package org.kwakmunsu.randsome.domain.inquiry.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;

@Builder
@Schema(description = "관리자용 문의 상세 응답 DTO")
public record InquiryReadAdminResponse(
        @Schema(description = "문의 ID", example = "1")
        Long inquiryId,

        @Schema(description = "작성자 ID", example = "1")
        Long authorId,

        @Schema(description = "작성자 닉네임", example = "작성자 닉네임입니다.")
        String authorNickname,

        @Schema(description = "문의 제목", example = "문의 제목입니다.")
        String title,

        @Schema(description = "문의 내용", example = "문의 내용입니다.")
        String content,

        @Schema(description = "답변 내용(답변이 안달렸을 경우 null)", example = "답변 내용입니다.")
        String answer,

        @Schema(description = "문의 상태", example = "대기 | 완료")
        String state,

        @Schema(description = "문의 생성 일시", example = "2023-10-01T12:34:56")
        LocalDateTime createdAt
) {

    public InquiryReadAdminResponse(
            Long inquiryId,
            Long authorId,
            String authorNickname,
            String title,
            String content,
            String answer,
            InquiryStatus stateEnum,
            LocalDateTime createdAt
    ) {
        this(
                inquiryId,
                authorId,
                authorNickname,
                title,
                content,
                answer,
                stateEnum.getDescription(),
                createdAt
        );
    }

}