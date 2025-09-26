package org.kwakmunsu.randsome.domain.member.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.NOT_FOUND;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberRegisterRequest;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member API", description = "회원 관련 API 문서입니다.")
public abstract class MemberDocsController {

    @Operation(
            summary = "회원 가입",
            description = """
            ### 신규 회원을 등록한다.
            - 201 Created 상태 코드를 반환한다.
            - 요청 본문은 검증 규칙에 따라 유효성 검사를 수행한다.
            """,
            security = { @SecurityRequirement(name = "") }
    )
    @RequestBody(
            description = """
                   회원 가입 요청 본문
                   - 모든 필드는 필수입니다.
                   
                   """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MemberRegisterRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "고객 회원 생성 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Long.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Long> register(MemberRegisterRequest request);

}