package org.kwakmunsu.randsome.admin.statistics.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.statistics.service.dto.MatchingStatisticsResponse;
import org.kwakmunsu.randsome.admin.statistics.service.StatisticsAdminService;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Statistics Admin API", description = "관리자 통계 조회 API 문서입니다.")
public abstract class StatisticsAdminDocsController {

    @Operation(
            summary = "관리자용 매칭 통계 조회 - [JWT O]",
            description = """
                    ### 관리자 페이지에서 매칭 관련 통계 데이터를 조회합니다.
                    - 관리자 권한이 필요합니다.
                    - 총 회원 수, 후보자 수, 매칭 수 등 주요 통계 정보 반환
                    - 200 OK 상태 코드와 함께 응답됩니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "매칭 통계 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MatchingStatisticsResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MatchingStatisticsResponse> getStatistics();

}