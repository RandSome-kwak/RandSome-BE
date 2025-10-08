package org.kwakmunsu.randsome.domain.matching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MatchingApplicationRepository matchingApplicationRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MatchingService matchingService;

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
        var matchingApplicationListResponse = matchingService.getMatchingApplication(requester.getId(), MatchingStatus.COMPLETED);

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
        var matchingApplicationListResponse = matchingService.getMatchingApplication(requester.getId(), MatchingStatus.PENDING);

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
        var matchingApplicationListResponse = matchingService.getMatchingApplication(1L, MatchingStatus.COMPLETED);

        // then
        var applicationPreviewResponses = matchingApplicationListResponse.responses();
        assertThat(applicationPreviewResponses).isEmpty();
    }

    private List<MatchingApplication> createMatchingApplication(Member requester) {
        List<MatchingApplication> matchingApplications = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            var application = MatchingApplication.create(requester, MatchingType.IDEAL_MATCHING, 3);
            if (i % 3 == 0) {
                application.complete();
            } else if (i % 3 == 2) {
                application.fail();
            }
            matchingApplications.add(application);
        }

        return matchingApplications;
    }

}