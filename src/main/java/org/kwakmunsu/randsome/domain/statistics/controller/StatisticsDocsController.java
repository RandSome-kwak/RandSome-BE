package org.kwakmunsu.randsome.domain.statistics.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.FORBIDDEN_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.domain.statistics.service.dto.MatchingStatisticsResponse;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Statistics API", description = "통계 조회 API 문서입니다.")
public abstract class StatisticsDocsController {

    @Operation(
            summary = "매칭 통계 조회",
            description = """
                    매칭 통계 정보를 조회합니다.
                    - 총 매칭 후보자 수: 승인된 매칭 후보자의 총 수
                    - 오늘 매칭 신청 수: 오늘 날짜에 생성된 매칭 신청의 수
                    - 총 매칭 수: 완료된 매칭의 총 수
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