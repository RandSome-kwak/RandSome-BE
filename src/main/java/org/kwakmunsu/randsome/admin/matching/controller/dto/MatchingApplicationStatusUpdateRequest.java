package org.kwakmunsu.randsome.admin.matching.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;

@Builder
@Schema(description = "매칭 신청 상태 업데이트 요청 DTO")
public record MatchingApplicationStatusUpdateRequest(
        @Schema(description = "매칭 상태", example = "COMPLETED | FAILED")
        @NotNull(message = "매칭 상태는 필수입니다.")
        MatchingStatus status
) {

}