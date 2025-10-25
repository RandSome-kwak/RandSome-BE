package org.kwakmunsu.randsome.domain.matching.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.FORBIDDEN_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.kwakmunsu.randsome.domain.matching.controller.dto.MatchingApplicationRequest;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingEventResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingReadResponse;
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
            security = {@SecurityRequirement(name = "Bearer ")}
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

    @Operation(
            summary = "매칭 신청 목록 조회 - [JWT O]",
            description = """
                    ### 회원의 매칭 신청 목록을 상태별로 조회합니다.
                    - 로그인한 회원 본인의 매칭 신청 목록만 조회할 수 있습니다.
                    - 매칭 상태(PENDING, COMPLETED, FAILED)를 필터링하여 조회합니다.
                    - 필터링은 (대기,취소) 및 (완료) 두 가지 유형으로 가능합니다.
                    - 200 OK 상태 코드와 함께 매칭 신청 목록을 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @Parameter(
            name = "matchingStatus",
            description = """
                    조회할 매칭 신청 상태
                    - PENDING: 대기 | FAILED: 실패
                    - COMPLETED: 완료
                    """,
            required = true,
            in = ParameterIn.QUERY,
            schema = @Schema(implementation = MatchingStatus.class),
            example = "PENDING"
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
            Long requesterId,
            int pageSize,
            Long lastApplicationId,
            MatchingStatus status
    );

    @Operation(
            summary = "매칭 결과 조회 - [JWT O]",
            description = """
                    ### 회원의 매칭 결과를 조회합니다.
                    - 로그인한 회원 본인의 매칭 결과만 조회할 수 있습니다.
                    - 매칭이 완료된 신청에 대해서만 조회할 수 있습니다.
                    - 승인 대기중이거나 실패한 매칭 신청에 대해서는 조회할 수 없습니다.
                    - 200 OK 상태 코드와 함께 매칭 결과를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "매칭 결과 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MatchingReadResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MatchingReadResponse> getMatching(
            Long requesterId,
            @Parameter(
                    name = "applicationId",
                    description = "조회할 매칭 신청 ID",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(implementation = Long.class),
                    example = "1"
            )
            Long applicationId
    );


    @Operation(
            summary = "최근 매칭 소식 조회",
            description = """
                    ### 최근 5개의 매칭 소식을 조회합니다. (매칭 후보자 등록, 매칭 신청)
                    - 인증이 필요하지 않습니다.
                    - 최근 소식 5개를 생성일시 내림차순으로 정렬하여 반환합니다.
                    - 200 OK 상태 코드와 함께 매칭 소식 목록을 반환합니다.
                    """,
            security = {}
    )
    @ApiResponse(
            responseCode = "200",
            description = "최근 매칭 소식 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(implementation = MatchingEventResponse.class))
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<List<MatchingEventResponse>> getRecentMatchingNews();

}