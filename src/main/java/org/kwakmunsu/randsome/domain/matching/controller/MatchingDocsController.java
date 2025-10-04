package org.kwakmunsu.randsome.domain.matching.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.FORBIDDEN_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.domain.matching.controller.dto.MatchingApplicationRequest;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Matching API", description = "매칭 신청/진행 관련 API 문서입니다.")
public abstract class MatchingDocsController {

    @Operation(
            summary = "매칭 신청 - [JWT O]",
            description = """
                    ### 회원이 매칭을 신청합니다.
                    - 로그인한 회원만 신청할 수 있습니다.
                    - 매칭 신청 후 결제를 진행합니다.
                    - 201 Created 상태 코드와 함께 생성된 신청 ID를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @RequestBody(
            description = """
                    매칭 신청 요청 본문
                    - 매칭 타입과 필요한 추가 정보를 포함합니다.
                    - DTO 유효성 검증을 통과해야 합니다.
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MatchingApplicationRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "매칭 신청 생성 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Long.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Long> apply(
            MatchingApplicationRequest request,
            Long memberId
    );
}
