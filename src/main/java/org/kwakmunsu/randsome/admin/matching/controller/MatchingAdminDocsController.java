package org.kwakmunsu.randsome.admin.matching.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.FORBIDDEN_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Matching Admin API", description = "관리자 매칭 신청 관리 API 문서입니다.")
public abstract class MatchingAdminDocsController {

    @Operation(
            summary = "매칭 신청 목록 조회 - [JWT O]",
            description = """
                    ### 상태별 매칭 신청 목록을 조회합니다.
                    - 관리자 권한이 필요합니다.
                    - 상태별로 필터링하여 조회할 수 있습니다. (대기, 완료, 실패)
                    - 페이징 처리를 지원하며, 한 페이지당 20개의 항목을 반환합니다.
                    - 최신 신청 순으로 정렬됩니다.
                    - 200 OK 상태 코드와 함께 신청 목록을 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "매칭 신청 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MatchingApplicationListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MatchingApplicationListResponse> getApplications(
            @Parameter(
                    name = "status",
                    description = "조회할 매칭 신청 상태 (PENDING: 대기중, COMPLETED: 완료, FAILED: 실패)",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = MatchingStatus.class)
            )
            MatchingStatus status,

            @Parameter(
                    name = "page",
                    description = "페이지 번호 (최소값: 1, 기본값: 1)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", minimum = "1", defaultValue = "1")
            )
            int page
    );

}