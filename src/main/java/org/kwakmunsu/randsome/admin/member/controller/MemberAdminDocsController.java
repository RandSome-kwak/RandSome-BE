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
import jakarta.validation.constraints.Min;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.domain.member.repository.dto.MemberListResponse;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member Admin API", description = "관리자 회원 정보/목록 조회 API 문서입니다.")
public abstract class MemberAdminDocsController {

    @Operation(
            summary = "관리자용 회원 목록 조회 - [JWT O]",
            description = """
                    ### 서비스 등록 회원 목록을 조회합니다.
                    - 관리자 권한이 필요합니다.
                    - 페이지 단위로 회원 정보를 조회할 수 있습니다.
                    - 한 페이지에 20명씩 반환하며, totalCount와 hasNext 필드 포함.
                    - 200 OK 상태 코드와 함께 회원 목록 응답.
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
                    description = "페이지 번호 (최소값: 1, 기본값: 1)",
                    in = ParameterIn.QUERY,
                    required = false,
                    schema = @Schema(type = "integer", minimum = "1", defaultValue = "1")
            )
            @Min(1) int page
    );

    @Operation(
            summary = "회원 상세 조회 - [JWT O]",
            description = """
                    ### 개별 회원의 상세 정보를 조회합니다.
                    - 관리자 권한이 필요합니다.
                    - 회원 ID로 조회하며, 상세 기본 정보 반환.
                    - 200 OK 상태 코드와 함께 회원 상세 정보 응답.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 상세 정보 조회 성공",
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
                    description = "상세 정보를 조회할 회원 ID",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            Long memberId
    );

}