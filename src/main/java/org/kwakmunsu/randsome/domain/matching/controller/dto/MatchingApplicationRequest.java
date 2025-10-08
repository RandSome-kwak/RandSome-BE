package org.kwakmunsu.randsome.domain.matching.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationServiceRequest;

@Schema(description = "매칭 신청 요청")
public record MatchingApplicationRequest(
        @Schema(description = "매칭 유형", example = "  RANDOM_MATCHING | IDEAL_MATCHING")
        @NotNull(message = "매칭 유형은 필수입니다.")
        MatchingType type,

        @Schema(description = "매칭 인원", example = "2")
        @Min(value = 1, message = "매칭 인원은 1명 이상이어야 합니다.")
        @Max(value = 5, message = "매칭 인원은 2명 이하이어야 합니다.")
        int matchingCount
) {

    public MatchingApplicationServiceRequest toServiceRequest(Long memberId) {
        return MatchingApplicationServiceRequest.builder()
                .memberId(memberId)
                .type(type)
                .matchingCount(matchingCount)
                .build();
    }

}
