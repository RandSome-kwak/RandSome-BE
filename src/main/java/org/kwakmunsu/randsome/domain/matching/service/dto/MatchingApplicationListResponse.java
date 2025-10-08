package org.kwakmunsu.randsome.domain.matching.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "매칭 신청 목록 응답 DTO")
@Builder
public record MatchingApplicationListResponse(
    List<MatchingApplicationPreviewResponse> responses
) {

}