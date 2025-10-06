package org.kwakmunsu.randsome.admin.member.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.FORBIDDEN_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberListResponse;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member Admin API", description = "관리자 회원 관리 API 문서입니다.")
public abstract class MemberAdminDocsController {

    @Operation(
            summary = "회원 상세 정보 조회 - [JWT O]",
            description = """
                    ### 특정 회원의 상세 정보를 조회합니다.
                    - 관리자 권한이 필요합니다.
                    - 회원의 모든 정보를 조회할 수 있습니다.
                    - 200 OK 상태 코드와 함께 회원 정보를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 정보 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MemberDetailResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MemberDetailResponse> getMember(
            @Parameter(
                    name = "memberId",
                    description = "조회할 회원 ID",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            Long memberId
    );

    @Operation(
            summary = "회원 목록 조회 - [JWT O]",
            description = """
                    ### 전체 회원 목록을 조회합니다.
                    - 관리자 권한이 필요합니다.
                    - 페이징 처리를 지원합니다.
                    - 200 OK 상태 코드와 함께 회원 목록을 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MemberListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MemberListResponse> getMembers(
            @Parameter(
                    name = "page",
                    description = "페이지 번호 (기본값: 1)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", minimum = "1", defaultValue = "1")
            )
            int page
    );

}
