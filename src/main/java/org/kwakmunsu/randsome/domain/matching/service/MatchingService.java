package org.kwakmunsu.randsome.domain.matching.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.service.CandidateRepository;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingEventType;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationServiceRequest;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingEventResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingMemberResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingReadResponse;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.domain.payment.service.dto.PaymentEvent;
import org.kwakmunsu.randsome.global.exception.ForbiddenException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchingService {

    private final MemberRepository memberRepository;
    private final CandidateRepository candidateRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long applyMatching(MatchingApplicationServiceRequest request) {
        MatchingType matchingType = request.type();
        Member member = memberRepository.findById(request.memberId());

        MatchingApplication application = createAndSaveApplication(member, matchingType, request.matchingCount());

        log.info("[new MatchingApplication]. MatchingApplicationId = {}, memberId = {}", application.getId(), member.getId());

        eventPublisher.publishEvent(new PaymentEvent(member, matchingType.toPaymentType(), request.matchingCount()));

        return application.getId();
    }

    /**
     * 자신의 매칭 결과 조회
     **/
    public MatchingReadResponse getMatching(Long requesterId, Long applicationId) {
        MatchingApplication application = matchingApplicationRepository.findByIdWithMatchings(applicationId);

        validateOwnership(application, requesterId);
        validateCompletionStatus(application);

        List<MatchingMemberResponse> responses = application.getMatchings().stream()
                .map(matching -> MatchingMemberResponse.from(matching.getSelectedMember())).toList();

        return MatchingReadResponse.of(application, responses);
    }

    /**
     * 자신의 매칭 신청 목록 조회
     **/
    public MatchingApplicationListResponse getMatchingApplication(Long requesterId, MatchingStatus status) {
        List<MatchingApplication> applications = status.isCompleted() ?
                findCompletedApplications(requesterId) : findPendingOrFailedApplications(requesterId);

        List<MatchingApplicationPreviewResponse> previewResponses = applications.stream()
                .map(MatchingApplicationPreviewResponse::of)
                .toList();

        return new MatchingApplicationListResponse(previewResponses);
    }

    /**
     * 5개의 최근 매칭 소식을 가져온다. (매칭 후보자 등록, 매칭 신청)
     **/
    public List<MatchingEventResponse> getRecentMatchingNews(int limit) {
        List<Candidate> candidates = candidateRepository.findRecentApplicationByOrderByCreatedAtDesc(limit);
        List<MatchingApplication> applications = matchingApplicationRepository.findRecentApplicationByOrderByCreatedAtDesc(limit);

        return Stream.concat(
                        candidates.stream().map(this::toCandidateEvent),
                        applications.stream().map(this::toMatchingApplicationEvent)
                )
                .sorted(Comparator.comparing(MatchingEventResponse::createdAt).reversed())
                .limit(limit)
                .toList();
    }

    private MatchingApplication createAndSaveApplication(Member member, MatchingType matchingType, int matchingCount) {
        MatchingApplication matchingApplication = MatchingApplication.create(member, matchingType, matchingCount);
        return matchingApplicationRepository.save(matchingApplication);
    }

    private List<MatchingApplication> findCompletedApplications(Long requesterId) {
        return matchingApplicationRepository.findAllByRequesterIdAndStatus(requesterId, MatchingStatus.COMPLETED);
    }

    private List<MatchingApplication> findPendingOrFailedApplications(Long requesterId) {
        return matchingApplicationRepository.findAllByRequesterIdAndStatusIn(requesterId,
                List.of(MatchingStatus.PENDING, MatchingStatus.FAILED));
    }

    private void validateOwnership(MatchingApplication application, Long requesterId) {
        if (!application.isOwnedBy(requesterId)) {
            throw new ForbiddenException(ErrorStatus.FORBIDDEN_READ_MATCHING_OWNER);
        }
    }

    private void validateCompletionStatus(MatchingApplication application) {
        if (!application.isComplete()) {
            throw new ForbiddenException(ErrorStatus.FORBIDDEN_READ_MATCHING);
        }
    }

    private MatchingEventResponse toCandidateEvent(Candidate candidate) {
        String message = String.format("%s 님이 매칭 후보자로 신청했습니다.", candidate.getMember().getNickname());
        return MatchingEventResponse.from(MatchingEventType.CANDIDATE, message, candidate.getCreatedAt());
    }

    private MatchingEventResponse toMatchingApplicationEvent(MatchingApplication application) {
        String message = String.format("%s 님이 사랑을 찾기 위해 %d 매칭을 신청했습니다.",
                application.getRequester().getNickname(), application.getRequestedCount());
        return MatchingEventResponse.from(MatchingEventType.MATCHING, message, application.getCreatedAt());
    }

}