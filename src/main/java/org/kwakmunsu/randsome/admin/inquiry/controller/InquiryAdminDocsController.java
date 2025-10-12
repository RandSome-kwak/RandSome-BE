package org.kwakmunsu.randsome.admin.inquiry.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.FORBIDDEN_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.admin.inquiry.controller.dto.AnswerRegisterRequest;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryState;
import org.kwakmunsu.randsome.domain.inquiry.repository.dto.InquiryListAdminResponse;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Inquiry Admin API", description = "관리자 문의 관리 API 문서입니다.")
public abstract class InquiryAdminDocsController {

    @Operation(
            summary = "문의 답변 등록 - [JWT O]",
            description = """
                    ### 회원의 문의에 대한 답변을 등록합니다.
                    - 관리자 권한이 필요합니다.
                    - 답변 등록 시 문의 상태가 자동으로 '답변 완료'로 변경됩니다.
                    - 답변 등록 후 해당 회원에게 알림이 전송됩니다. (미정)
                    - 200 OK 상태 코드를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AnswerRegisterRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "답변 등록 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> registerAnswer(
            @Parameter(description = "답변을 등록할 문의 ID", example = "1", required = true)
            Long inquiryId,
            AnswerRegisterRequest request
    );

    @Operation(
            summary = "문의 목록 조회 - [JWT O]",
            description = """
                    ### 회원의 문의 목록을 조회합니다.
                    - 관리자 권한이 필요합니다.
                    - 문의 상태(대기, 완료)로 필터링할 수 있습니다.
                    - 페이지네이션을 지원합니다. (페이지당 20개)
                    - 200 OK 상태 코드를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "문의 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InquiryListAdminResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<InquiryListAdminResponse> getInquires(
            InquiryState state,
            int page
    );

}