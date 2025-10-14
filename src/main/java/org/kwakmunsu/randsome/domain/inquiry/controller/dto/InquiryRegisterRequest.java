package org.kwakmunsu.randsome.domain.inquiry.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryRegisterServiceRequest;

@Builder
@Schema(description = "문의 등록 요청 DTO")
public record InquiryRegisterRequest(
        @Schema(description = "문의 제목", example = "서비스 이용 관련 문의")
        String title,
        @Schema(description = "문의 내용", example = "서비스 이용 중 문제가 발생했습니다. 확인 부탁드립니다.")
        String content
) {

    public InquiryRegisterServiceRequest toServiceRequest(Long authorId) {
        return InquiryRegisterServiceRequest.builder()
                .authorId(authorId)
                .title(this.title)
                .content(this.content)
                .build();
    }

}