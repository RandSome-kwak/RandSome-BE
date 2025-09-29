package org.kwakmunsu.randsome.admin.candidate.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;

@Schema(description = "후보자 승인 여부 업데이트 요청 DTO")
public record CandidateStatusUpdateRequest(
        @Schema(description = "후보자 승인 상태", example = "APPROVED 또는 REJECTED")
        @NotNull(message = "승인 상태는 필수입니다.")
        CandidateStatus status
) {

}