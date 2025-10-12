package org.kwakmunsu.randsome.domain.inquiry.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.FORBIDDEN_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.randsome.domain.inquiry.serivce.dto.InquiryListResponse;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Inquiry API", description = "문의 관련 API 문서입니다.")
public abstract class InquiryDocsController {

    @Operation(
            summary = "문의 등록 - [JWT O]",
            description = """
                    ### 회원이 문의를 등록합니다.
                    - 로그인한 회원만 문의를 등록할 수 있습니다.
                    - 문의 제목, 내용 을 입력합니다.
                    - 201 Created 상태 코드와 함께 생성된 문의 ID를 반환합니다.
                    - 문의 등록 후 답변이 등록 되면 수정 및 삭제가 불가능합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InquiryRegisterRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "문의 등록 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Long.class, example = "1")
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Long> register(Long memberId, InquiryRegisterRequest request);

    @Operation(
            summary = "자신의 문의 내역 조회 - [JWT O]",
            description = """
                    ### 회원이 자신의 문의 내역을 조회합니다.
                    - 로그인한 회원만 자신의 문의 내역을 조회할 수 있습니다.
                    - 문의 ID, 작성자 닉네임, 문의 제목, 문의 내용, 답변 내용(답변이 없을 경우 null), 답변 여부, 문의 생성 일시 를
                      포함한 문의 리스트를 반환합니다.
                    - 문의가 없을 경우 빈 리스트를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "문의 내역 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InquiryListResponse.class)
            )
    )
    @ApiExceptions(values = {
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<InquiryListResponse> getInquires(Long memberId);

}