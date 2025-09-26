package org.kwakmunsu.randsome.domain.member.serivce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "중복 체크 응답 DTO")
public record CheckResponse(
        @Schema(description = "사용 가능 여부", example = "true")
        boolean available
) {

}