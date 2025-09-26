package org.kwakmunsu.randsome.global.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "토큰 응답 DTO")
@Builder
public record TokenResponse(
        @Schema(example = "new-access-token")
        String accessToken,

        @Schema(example = "new-refresh-token")
        String refreshToken
) {

}