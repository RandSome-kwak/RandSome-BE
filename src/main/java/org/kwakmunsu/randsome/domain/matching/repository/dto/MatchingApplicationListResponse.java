package org.kwakmunsu.randsome.domain.matching.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "매칭 신청 목록 응답 DTO")
@Builder
public record MatchingApplicationListResponse(
        @Schema(description = "매칭 신청 목록")
        List<MatchingApplicationPreviewResponse> responses,

        @Schema(description = "다음 페이지 존재 여부", example = "true | false")
        boolean hasNext,

        @Schema(description = "총 매칭 신청 수", example = "100")
        Long totalCount
) {

}
