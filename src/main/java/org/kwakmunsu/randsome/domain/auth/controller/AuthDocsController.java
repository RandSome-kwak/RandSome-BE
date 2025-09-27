package org.kwakmunsu.randsome.domain.auth.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
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
import org.kwakmunsu.randsome.domain.auth.controller.dto.LoginRequest;
import org.kwakmunsu.randsome.global.jwt.dto.TokenResponse;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth API", description = "인증 관련 API 문서입니다.")
public abstract class AuthDocsController {
    @Operation(
            summary = "로그인 요청 API - [JWT X}",
            description = """
                    ### 사용자 로그인
                    - 사용자의 로그인 ID와 비밀번호를 받아 인증을 수행합니다.
                    - 인증에 성공하면 액세스 토큰과 리프레시 토큰을 발급합니다.
                    - 요청 본문은 유효성 검사를 수행합니다.
                    """,
            security = {@SecurityRequirement(name = "")}
    )
    @RequestBody(
            description = """
                    로그인 요청 본문
                    - 모든 필드는 필수입니다.
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TokenResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<TokenResponse> login(LoginRequest request);

}