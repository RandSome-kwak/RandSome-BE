package org.kwakmunsu.randsome.domain.candidate.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.DUPLICATE;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Candidate API", description = "매칭 지원자 관련 API 문서입니다.")
public abstract class CandidateDocsController {

    @Operation(
            summary = "매칭 지원 등록 - [JWT O]",
            description = """
                    ### 회원이 매칭 서비스 후보에 신청합니다.
                    - 로그인한 회원만 지원할 수 있습니다.
                    - 이미 신청한 회원은 중첩 신청할 수 없습니다.
                    - 200 OK 상태 코드를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "매칭 신청 등록 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            NOT_FOUND,
            DUPLICATE,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> register(Long memberId);

}