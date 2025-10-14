package org.kwakmunsu.randsome.admin.matching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.matching.entity.Matching;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingRepository;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MatchingAdminServiceTest {

    @Mock
    private MatchingRepository matchingRepository;

    @Mock
    private MatchingApplicationRepository applicationRepository;

    @Mock
    private MatchingProvider randomMatchingProvider;

    @Mock
    private MatchingProvider idealMatchingProvider;

    @Captor
    private ArgumentCaptor<List<Matching>> matchingListCaptor;

    private MatchingAdminService matchingAdminService;

    private Member requester;
    private MatchingApplication application;
    private List<Member> matchedMembers;

    @BeforeEach
    void setUp() {
        given(randomMatchingProvider.getType()).willReturn(MatchingType.RANDOM_MATCHING);
        given(idealMatchingProvider.getType()).willReturn(MatchingType.IDEAL_MATCHING);

        matchingAdminService = new MatchingAdminService(
                matchingRepository,
                applicationRepository,
                List.of(randomMatchingProvider, idealMatchingProvider)
        );

        // 테스트 데이터
        requester = MemberFixture.createMember(1L, "requester");
        application = createApplication(1L, requester, MatchingType.RANDOM_MATCHING, 3);
        matchedMembers = List.of(
                MemberFixture.createMember(10L, "candidate1"),
                MemberFixture.createMember(11L, "candidate2"),
                MemberFixture.createMember(12L, "candidate3")
        );
    }

    @DisplayName("매칭 신청을 승인하면 매칭을 실행하고 상태를 COMPLETED로 변경한다")
    @Test
    void updateApplicationStatusApprove() {
        // given
        given(applicationRepository.findById(1L)).willReturn(application);
        given(randomMatchingProvider.match(eq(requester), eq(3))).willReturn(matchedMembers);

        // when
        matchingAdminService.updateApplicationStatus(1L, MatchingStatus.COMPLETED);

        // then
        assertThat(application.getStatus()).isEqualTo(MatchingStatus.COMPLETED);
        then(randomMatchingProvider).should(times(1)).match(eq(requester), eq(3));
        then(matchingRepository).should(times(1)).saveAll(anyList());
    }

    @DisplayName("매칭 신청을 거절하면 상태를 FAILED로 변경하고 매칭을 실행하지 않는다")
    @Test
    void updateApplicationStatusReject() {
        // given
        given(applicationRepository.findById(1L)).willReturn(application);

        // when
        matchingAdminService.updateApplicationStatus(1L, MatchingStatus.FAILED);

        // then
        assertThat(application.getStatus()).isEqualTo(MatchingStatus.FAILED);
        then(randomMatchingProvider).should(never()).match(any(Member.class), anyInt());
        then(matchingRepository).should(never()).saveAll(anyList());
    }

    @DisplayName("매칭 실행 시 Provider 에서 반환한 회원 수만큼 Matching 엔티티를 생성한다")
    @Test
    void executeMatchingCreatesCorrectNumberOfMatchings() {
        // given
        given(applicationRepository.findById(1L)).willReturn(application);
        given(randomMatchingProvider.match(eq(requester), eq(3))).willReturn(matchedMembers);

        // when
        matchingAdminService.updateApplicationStatus(1L, MatchingStatus.COMPLETED);

        // then
        then(matchingRepository).should().saveAll(matchingListCaptor.capture());
        List<Matching> savedMatchings = matchingListCaptor.getValue();

        assertThat(savedMatchings).hasSize(3);
        assertThat(savedMatchings)
                .extracting(m -> m.getSelectedMember().getId())
                .containsExactlyInAnyOrder(10L, 11L, 12L);
    }

    @DisplayName("이상형 매칭 타입일 때 IdealMatchingProvider를 사용한다")
    @Test
    void executeMatchingUsesCorrectProvider() {
        // given
        MatchingApplication idealApplication = createApplication(
                2L, requester, MatchingType.IDEAL_MATCHING, 3
        );
        given(applicationRepository.findById(2L)).willReturn(idealApplication);
        given(idealMatchingProvider.match(eq(requester), eq(3))).willReturn(matchedMembers);

        // when
        matchingAdminService.updateApplicationStatus(2L, MatchingStatus.COMPLETED);

        // then
        then(idealMatchingProvider).should(times(1)).match(eq(requester), eq(3));
        then(randomMatchingProvider).should(never()).match(any(Member.class), anyInt());
    }

    @DisplayName("존재하지 않는 신청 ID로 상태 변경 시 예외가 발생한다")
    @Test
    void updateApplicationStatusNotFound() {
        // given
        Long invalidId = 999L;
        given(applicationRepository.findById(invalidId))
                .willThrow(new NotFoundException(ErrorStatus.NOT_FOUND_MATCHING_APPLICATION));

        // when & then
        assertThatThrownBy(() ->
                matchingAdminService.updateApplicationStatus(invalidId, MatchingStatus.COMPLETED)
        ).isInstanceOf(NotFoundException.class);
    }

    private MatchingApplication createApplication(Long id, Member requester, MatchingType type, int requestedCount) {
        MatchingApplication app = MatchingApplication.create(requester, type, requestedCount);
        ReflectionTestUtils.setField(app, "id", id);
        return app;
    }

}
