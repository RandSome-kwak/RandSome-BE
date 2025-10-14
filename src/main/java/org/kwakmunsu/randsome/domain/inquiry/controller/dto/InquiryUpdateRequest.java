package org.kwakmunsu.randsome.domain.inquiry.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryUpdateServiceRequest;

@Schema(description = "문의 수정 요청 DTO")
@Builder
public record InquiryUpdateRequest(

        @Schema(description = "문의 제목", example = "서비스 이용 관련 문의")
        @NotBlank(message = "문의 제목은 필수 입력 값입니다.")
        String title,

        @Schema(description = "문의 내용", example = "서비스 이용 중 문제가 발생했습니다. 확인 부탁드립니다.")
        @NotBlank(message = "문의 내용은 필수 입력 값입니다.")
        String content
) {

    public InquiryUpdateServiceRequest toServiceRequest(Long inquiryId, Long authorId) {
        return InquiryUpdateServiceRequest.builder()
                .inquiryId(inquiryId)
                .authorId(authorId)
                .title(this.title)
                .content(this.content)
                .build();
    }

}