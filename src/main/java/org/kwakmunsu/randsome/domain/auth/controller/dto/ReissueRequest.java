package org.kwakmunsu.randsome.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "토큰 재발급 요청 DTO")
public record ReissueRequest(
        @Schema(description = "리프레쉬 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9....")
        @NotBlank(message = "리프레쉬 토큰은 필수입니다.")
        String refreshToken
) {

}
