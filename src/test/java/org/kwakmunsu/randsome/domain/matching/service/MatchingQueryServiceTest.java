package org.kwakmunsu.randsome.domain.matching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.service.CandidateRepository;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingEventType;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingEventResponse;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MatchingQueryServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private MatchingApplicationRepository matchingApplicationRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MatchingQueryService matchingQueryService;

    @DisplayName("승인 완료된 매칭 신청 목록을 조회한다.")
    @Test
    void findMatchingApplicationsCompleted() {
        // given
        var requester = MemberFixture.createMember(1L);
        var matchingApplications = createMatchingApplication(requester);
        var completedApplications = matchingApplications.stream()
                .filter(app -> app.getMatchingStatus() == MatchingStatus.COMPLETED)
                .toList();

        given(matchingApplicationRepository.findAllByRequesterIdAndStatus(requester.getId(), MatchingStatus.COMPLETED))
                .willReturn(completedApplications);

        // when
        var matchingApplicationListResponse = matchingQueryService.getMatchingApplication(requester.getId(), MatchingStatus.COMPLETED);

        // then
        var applicationPreviewResponses = matchingApplicationListResponse.responses();
        assertThat(applicationPreviewResponses).hasSize(completedApplications.size());
        assertThat(applicationPreviewResponses)
                .extracting(MatchingApplicationPreviewResponse::matchingStatus)
                .containsOnly(MatchingStatus.COMPLETED.getDescription());
    }

    @DisplayName("대기 또는 실패한 매칭 신청 목록을 조회한다.")
    @Test
    void findMatchingApplicationsPendingAndFail() {
        // given
        var requester = MemberFixture.createMember(1L);
        var matchingApplications = createMatchingApplication(requester);
        var pendingAndFailApplications = matchingApplications.stream()
                .filter(app -> app.getMatchingStatus() != MatchingStatus.COMPLETED)
                .toList();

        given(matchingApplicationRepository.findAllByRequesterIdAndStatusIn(requester.getId(), List.of(MatchingStatus.PENDING, MatchingStatus.FAILED)))
                .willReturn(pendingAndFailApplications);

        // when
        var matchingApplicationListResponse = matchingQueryService.getMatchingApplication(requester.getId(), MatchingStatus.PENDING);

        // then
        var applicationPreviewResponses = matchingApplicationListResponse.responses();
        assertThat(applicationPreviewResponses).hasSize(pendingAndFailApplications.size());
        assertThat(applicationPreviewResponses)
                .extracting(MatchingApplicationPreviewResponse::matchingStatus)
                .containsOnly(
                        MatchingStatus.PENDING.getDescription(),
                        MatchingStatus.FAILED.getDescription()
                );
    }

    @DisplayName("신청 목록이 없을 경우 빈 목록을 반환한다.")
    @Test
    void returnEmpty() {
        // given
        given(matchingApplicationRepository.findAllByRequesterIdAndStatus(1L, MatchingStatus.COMPLETED))
                .willReturn(List.of());

        // when
        var matchingApplicationListResponse = matchingQueryService.getMatchingApplication(1L, MatchingStatus.COMPLETED);

        // then
        var applicationPreviewResponses = matchingApplicationListResponse.responses();
        assertThat(applicationPreviewResponses).isEmpty();
    }

    @DisplayName("최근 매칭 소식 5개를 조회한다.")
    @Test
    void getRecentMatchingNews() {
        // given
        var candidates = createCandidates();
        var member = MemberFixture.createMember(999L);
        var matchingApplications = createMatchingApplication(member);
        given(matchingApplicationRepository.findRecentApplicationByOrderByCreatedAtDesc(any(Integer.class))).willReturn(matchingApplications);
        given(candidateRepository.findRecentApplicationByOrderByCreatedAtDesc(any(Integer.class))).willReturn(candidates);
        var limit = 5;
        // when
        List<MatchingEventResponse> responses = matchingQueryService.getRecentMatchingNews(limit);

        // then
        assertThat(responses).hasSizeLessThanOrEqualTo(limit);
        assertThat(responses).allSatisfy(event -> {
            assertThat(event).isNotNull();
            assertThat(event.createdAt()).isNotNull();
            assertThat(event.eventType()).isIn(MatchingEventType.MATCHING.getDescription(), MatchingEventType.CANDIDATE.getDescription());
            assertThat(event.eventDescription()).isNotBlank();
        });

        // createdAt가 내림차순 정렬인지 검증 (앞이 크거나 같아야함)
        for (int i = 0; i < responses.size() - 1; i++) {
            LocalDateTime curr = responses.get(i).createdAt();
            LocalDateTime next = responses.get(i + 1).createdAt();
            assertThat(curr).isAfterOrEqualTo(next);
        }

    }

    private List<MatchingApplication> createMatchingApplication(Member requester) {
        List<MatchingApplication> matchingApplications = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            var application = MatchingApplication.create(requester, MatchingType.IDEAL_MATCHING, 3);
            ReflectionTestUtils.setField(application, "createdAt", LocalDateTime.now());
            if (i % 3 == 0) {
                application.complete();
            } else if (i % 3 == 2) {
                application.fail();
            }
            matchingApplications.add(application);
        }

        return matchingApplications;
    }

    private List<Candidate> createCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            var member = MemberFixture.createMember((long) i);
            var candidate = Candidate.create(member);
            ReflectionTestUtils.setField(candidate, "createdAt", LocalDateTime.now());
            candidates.add(candidate);
        }
        return candidates;

    }

}