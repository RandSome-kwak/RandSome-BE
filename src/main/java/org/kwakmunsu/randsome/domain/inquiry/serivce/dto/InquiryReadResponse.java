package org.kwakmunsu.randsome.domain.inquiry.serivce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;

@Schema(description = "문의 상세 응답 DTO")
@Builder
public record InquiryReadResponse(
        @Schema(description = "문의 ID", example = "1")
        Long inquiryId,

        @Schema(description = "작성자 닉네임", example = "작성자 닉네임입니다.")
        String authorNickname,

        @Schema(description = "문의 제목", example = "문의 제목입니다.")
        String title,

        @Schema(description = "문의 내용", example = "문의 내용입니다.")
        String content,

        @Schema(description = "답변 내용(답변이 안달렸을 경우 null)", example = "답변 내용입니다.")
        String answer,

        @Schema(description = "답변 여부", example = "true")
        Boolean answered,

        @Schema(description = "문의 생성 일시", example = "2023-10-01T12:34:56")
        LocalDateTime createdAt
) {

    public static InquiryReadResponse from(Inquiry inquiry) {
        return InquiryReadResponse.builder()
                .inquiryId(inquiry.getId())
                .authorNickname(inquiry.getAuthor().getNickname())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .answer(inquiry.getAnswer())
                .answered(inquiry.isAnswered())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

}