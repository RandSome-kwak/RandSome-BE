package org.kwakmunsu.randsome.domain.statistics.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "회원 통계 대시보드 응답 DTO")
@Builder
public record MatchingStatisticsResponse(
        @Schema(description = "총 매칭 후보자 수", example = "300")
        long totalCandidateCount,

        @Schema(description = "오늘 매칭 신청 수", example = "15")
        long todayApplicationCount,

        @Schema(description = "총 매칭 수", example = "1200")
        long totalMatchingCount
) {

    public static MatchingStatisticsResponse of(
            long totalCandidateCount,
            long todayApplicationCount,
            long totalMatchingCount
    ) {
        return MatchingStatisticsResponse.builder()
                .totalCandidateCount(totalCandidateCount)
                .todayApplicationCount(todayApplicationCount)
                .totalMatchingCount(totalMatchingCount)
                .build();
    }

}