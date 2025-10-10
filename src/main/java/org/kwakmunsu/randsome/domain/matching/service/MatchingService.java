package org.kwakmunsu.randsome.domain.matching.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationServiceRequest;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingMemberResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingReadResponse;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.kwakmunsu.randsome.domain.payment.serivce.dto.PaymentEvent;
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
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long matchingApply(MatchingApplicationServiceRequest request) {
        MatchingType matchingType = request.type();
        Member member = memberRepository.findById(request.memberId());

        MatchingApplication matchingApplication = MatchingApplication.create(member, matchingType, request.matchingCount());
        MatchingApplication application = matchingApplicationRepository.save(matchingApplication);
        log.info("[new MatchingApplication]. MatchingApplicationId = {}, memberId = {}", application.getId(), member.getId());

        eventPublisher.publishEvent(new PaymentEvent(member, matchingType.toPaymentType(), request.matchingCount()));

        return application.getId();
    }

    /**
     * 자신의 매칭 결과 조회
     **/

    public MatchingReadResponse getMatching(Long applicationId) {
        MatchingApplication application = matchingApplicationRepository.findByIdWithMatchings(applicationId);

        if (!application.isComplete()) {
            throw new ForbiddenException(ErrorStatus.FORBIDDEN_READ_MATCHING);
        }

        List<MatchingMemberResponse> responses = application.getMatchings().stream()
                .map(matching -> MatchingMemberResponse.from(matching.getSelectedMember())).toList();

        return MatchingReadResponse.of(application, responses);
    }

    /**
     * 자신의 매칭 신청 목록 조회
     **/
    public MatchingApplicationListResponse getMatchingApplication(Long requesterId, MatchingStatus status) {
        List<MatchingApplication> applications = status == MatchingStatus.COMPLETED ?
                findCompletedApplications(requesterId) : findPendingOrFailedApplications(requesterId);

        List<MatchingApplicationPreviewResponse> previewResponses = applications.stream()
                .map(MatchingApplicationPreviewResponse::of)
                .toList();

        return new MatchingApplicationListResponse(previewResponses);
    }

    /**
     * 자신의 매칭 신청 목록 조회 (완료)
     **/
    private List<MatchingApplication> findCompletedApplications(Long requesterId) {
        return matchingApplicationRepository.findAllByRequesterIdAndStatus(requesterId, MatchingStatus.COMPLETED);
    }

    /**
     * 자신의 매칭 신청 목록 조회 (대기, 실패)
     **/
    private List<MatchingApplication> findPendingOrFailedApplications(Long requesterId) {
        return matchingApplicationRepository.findAllByRequesterIdAndStatusIn(requesterId,
                List.of(MatchingStatus.PENDING, MatchingStatus.FAILED));
    }

}