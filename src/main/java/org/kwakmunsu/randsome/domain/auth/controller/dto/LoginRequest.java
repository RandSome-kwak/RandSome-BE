package org.kwakmunsu.randsome.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "로그인 요청 DTO")
public record LoginRequest(
        @Schema(description = "로그인 아이디", example = "user123")
        @NotBlank(message = "loginId는 필수입니다.")
        String loginId,

        @Schema(description = "비밀번호", example = "password123!")
        @Size(min = 8, message = "password는 최소 8자 이상이어야 합니다.")
        @NotBlank(message = "password는 필수입니다.")
        String password
) {

}