package org.kwakmunsu.randsome.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.randsome.domain.matching.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.matching.service.MatchingRepository;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberActivityStatsResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberActivityStatsServiceTest {

    @Mock
    private MatchingApplicationRepository matchingApplicationRepository;

    @Mock
    private MatchingRepository matchingRepository;

    @Mock
    private InquiryRepository inquiryRepository;

    @InjectMocks
    private MemberActivityStatsService memberActivityStatsService;

    @DisplayName("회원이 활동한 매칭 신청, 받은 매칭, 문의 횟수를 조회")
    @Test
    void getMemberActivityInfo() {
        // given
        given(inquiryRepository.countByAuthorIdAndStatus(any(Long.class), any(EntityStatus.class))).willReturn(5L);
        given(matchingRepository.countBySelectedMemberIdAndStatus(any(Long.class), any(EntityStatus.class))).willReturn(5L);
        given(matchingApplicationRepository.countByRequesterIdAndStatus(any(Long.class), any(EntityStatus.class))).willReturn(5L);

        // when
        MemberActivityStatsResponse response = memberActivityStatsService.getMemberActivityInfo(1L);

        // then

        assertThat(response).extracting(
                        MemberActivityStatsResponse::appliedCount,
                        MemberActivityStatsResponse::selectedCount,
                        MemberActivityStatsResponse::inquiryCount
                )
                .containsExactly(5L, 5L, 5L);
    }

}