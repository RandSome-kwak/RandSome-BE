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
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingEventResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingMemberResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingReadResponse;
import org.kwakmunsu.randsome.global.exception.ForbiddenException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchingQueryService {

    private final CandidateRepository candidateRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;

    /**
     * 자신의 매칭 결과 조회
     **/
    public MatchingReadResponse getMatching(Long requesterId, Long applicationId) {
        MatchingApplication application = matchingApplicationRepository.findByIdWithMatchings(applicationId);

        validateOwnership(application, requesterId);
        validateCompletionStatus(application);

        List<MatchingMemberResponse> responses = application.getMatchings().stream()
                .map(matching -> MatchingMemberResponse.from(matching.getSelectedMember()))
                .toList();

        return MatchingReadResponse.of(application, responses);
    }

    /**
     * 자신의 매칭 신청 목록 조회
     **/
    public MatchingApplicationListResponse getMatchingApplication(Long requesterId, MatchingStatus status) {
        List<MatchingApplication> applications = status.isCompleted() ? findCompletedApplications(requesterId) : findPendingOrFailedApplications(requesterId);

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