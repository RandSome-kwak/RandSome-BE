package org.kwakmunsu.randsome.admin.inquiry.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "관리자 답변 등록 요청 DTO")
public record AnswerRegisterRequest(
        @Schema(description = "답변 내용", example = "문의에 대한 답변입니다.", requiredMode = RequiredMode.REQUIRED)
        @NotBlank(message = "답변 내용은 필수입니다.")
        String answer
) {

}