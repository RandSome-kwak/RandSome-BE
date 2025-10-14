package org.kwakmunsu.randsome.admin.statistics.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "매칭 통계 응답 DTO")
@Builder
public record MatchingStatisticsAdminResponse(
        @Schema(description = "총 회원 수", example = "1500")
        long totalMemberCount,

        @Schema(description = "매칭 후보자 수", example = "300")
        long totalCandidateCount,

        @Schema(description = "총 매칭 신청 수", example = "1200")
        long totalMatchingCount,

        @Schema(description = "승인 대기 수 (후보자, 매칭 신청, 문의 등)", example = "25")
        long pendingApprovalsCount      // 승인 대기 수 (후보자, 매칭 신청, 문의 등)
) {

    public static MatchingStatisticsAdminResponse of(
            long totalMemberCount,
            long totalCandidateCount,
            long totalMatchingCount,
            long pendingApprovals
    ) {
        return MatchingStatisticsAdminResponse.builder()
                .totalMemberCount(totalMemberCount)
                .totalCandidateCount(totalCandidateCount)
                .totalMatchingCount(totalMatchingCount)
                .pendingApprovalsCount(pendingApprovals)
                .build();
    }

}