package org.kwakmunsu.randsome.domain.statistics.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.service.CandidateRepository;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.service.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.statistics.service.dto.MatchingStatisticsResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private MatchingApplicationRepository matchingApplicationRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @DisplayName("회원용 통계 대시보드 정보를 조회한다.")
    @Test
    void getStatistics() {
        // given
        given(candidateRepository.countByStatus(any(CandidateStatus.class))).willReturn(150L);
        given(matchingApplicationRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class))
        ).willReturn(10L);
        given(matchingApplicationRepository.countByStatus(any(MatchingStatus.class))).willReturn(25L);

        // when
        MatchingStatisticsResponse response = statisticsService.getMatchingStatistics();

        // then
        assertThat(response).extracting(
                MatchingStatisticsResponse::totalCandidateCount,
                MatchingStatisticsResponse::todayApplicationCount,
                MatchingStatisticsResponse::totalMatchingCount
        ).containsExactly(
                150L,
                10L,
                25L
        );
    }

}