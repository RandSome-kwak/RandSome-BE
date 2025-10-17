package org.kwakmunsu.randsome.admin.candidate.controller;

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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.admin.candidate.controller.dto.CandidateStatusUpdateRequest;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Candidate Admin API", description = "관리자 매칭 지원자 관리 API 문서입니다.")
public abstract class CandidateAdminDocsController {

    @Operation(
            summary = "매칭 지원자 상태 변경 - [JWT O]",
            description = """
                    ### 매칭 지원자의 상태를 변경합니다.
                    - 관리자 권한이 필요합니다.
                    - 지원자의 승인/거절/대기 상태를 변경할 수 있습니다.
                    - 200 OK 상태 코드를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @RequestBody(
            description = """
                    지원자 상태 변경 요청 본문
                    - status: 변경할 상태 (APPROVED, REJECTED)
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CandidateStatusUpdateRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "지원자 상태 변경 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> approve(
            @Parameter(
                    name = "candidateId",
                    description = "상태를 변경할 지원자 ID",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            Long candidateId,
            CandidateStatusUpdateRequest request
    );

    @Operation(
            summary = "매칭 지원자 목록 조회 - [JWT O]",
            description = """
                    ### 상태별 매칭 지원자 목록을 조회합니다.
                    - 관리자 권한이 필요합니다.
                    - 상태별로 필터링하여 조회할 수 있습니다.
                    - 페이징 처리를 지원합니다.
                    - 200 OK 상태 코드와 함께 지원자 목록을 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "지원자 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CandidateListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<CandidateListResponse> getCandidateApplications(
            @Parameter(
                    name = "status",
                    description = "조회할 지원자 상태 (PENDING, APPROVED, REJECTED)",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = CandidateStatus.class)
            )
            CandidateStatus status,

            @Parameter(
                    name = "page",
                    description = "페이지 번호 (기본값: 1)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", minimum = "1", defaultValue = "1")
            )
            int page
    );

}