package org.kwakmunsu.randsome.admin.statistics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.admin.statistics.service.dto.MatchingStatisticsResponse;
import org.kwakmunsu.randsome.global.security.annotation.TestAdmin;
import org.springframework.http.MediaType;

class StatisticsAdminControllerTest extends ControllerTestSupport {

    @TestAdmin
    @DisplayName("관리자가 매칭 통계를 조회한다.")
    @Test
    void getStatistics() {
        // given
        var matchingStatisticsResponse = MatchingStatisticsResponse.builder()
                .totalMemberCount(100)
                .totalCandidateCount(50)
                .totalMatchingCount(30)
                .pendingApprovalsCount(10)
                .build();
        given(statisticsAdminService.getMatchingStatistics()).willReturn(matchingStatisticsResponse);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/statistics")
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.totalMemberCount", v -> assertThat(v).isEqualTo(100))
                .hasPathSatisfying("$.totalCandidateCount", v -> assertThat(v).isEqualTo(50))
                .hasPathSatisfying("$.totalMatchingCount", v -> assertThat(v).isEqualTo(30))
                .hasPathSatisfying("$.pendingApprovalsCount", v -> assertThat(v).isEqualTo(10));
    }

}