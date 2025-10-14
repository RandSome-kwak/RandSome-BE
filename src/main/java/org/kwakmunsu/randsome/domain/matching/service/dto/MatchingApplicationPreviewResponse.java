package org.kwakmunsu.randsome.domain.matching.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;

@Schema(description = "매칭 신청 목록 정보 응답 DTO")
@Builder
public record MatchingApplicationPreviewResponse(
        @Schema(description = "매칭 신청 ID", example = "1")
        Long applicationId,

        @Schema(description = "요청한 매칭 수", example = "3")
        int requestedCount,

        @Schema(description = "매칭 상태", example = "대기 | 완료 | 실패")
        String matchingStatus,

        @Schema(description = "매칭 타입", example = " 랜덤 | 이상형")
        String matchingType,

        @Schema(description = "매칭 신청일", example = "2023-10-05T14:48:00")
        LocalDateTime appliedAt
) {

    public static MatchingApplicationPreviewResponse of(MatchingApplication application) {
        return MatchingApplicationPreviewResponse.builder()
                .applicationId(application.getId())
                .requestedCount(application.getRequestedCount())
                .matchingStatus(application.getStatus().getDescription())
                .matchingType(application.getMatchingType().getDescription())
                .appliedAt(application.getCreatedAt())
                .build();
    }

}