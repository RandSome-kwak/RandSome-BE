package org.kwakmunsu.randsome.domain.matching.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "매칭 신청 목록 응답 DTO")
@Builder
public record MatchingApplicationListResponse(
        List<MatchingApplicationPreviewResponse> responses,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext,

        @Schema(description = "마지막 매칭 신청 ID", example = "123")
        Long lastApplicationId
) {

}