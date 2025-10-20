package org.kwakmunsu.randsome.admin.matching.controller;

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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.matching.controller.dto.MatchingApplicationUpdateRequest;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminListResponse;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
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
                    schema = @Schema(implementation = MatchingApplicationAdminListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<PageResponse<MatchingApplicationAdminPreviewResponse>> getApplications(
            @Parameter(
                    name = "matchingStatus",
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
            @Min(1) int page
    );

    @Operation(
            summary = "매칭 신청 상태 변경 - [JWT O]",
            description = """
                    ### 매칭 신청의 상태를 변경합니다.
                    - 관리자 권한이 필요합니다.
                    - COMPLETED(완료)로 변경 시 자동으로 매칭이 실행됩니다.
                    - FAILED(실패)로 변경 시 매칭 신청이 거절됩니다.
                    - 상태 변경 후 해당 회원에게 알림이 전송됩니다.(미정)
                    - 200 OK 상태 코드를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @RequestBody(
            description = """
                    상태 변경 요청 본문
                    - matchingStatus: 변경할 상태 (COMPLETED, FAILED)
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MatchingApplicationUpdateRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "상태 변경 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> updateApplication(
            @Parameter(
                    name = "applicationId",
                    description = "상태를 변경할 매칭 신청 ID",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            Long applicationId,

            @Valid MatchingApplicationUpdateRequest request
    );

}