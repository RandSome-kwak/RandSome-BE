package org.kwakmunsu.randsome.admin.inquiry.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "문의 신청 목록 응답 DTO")
@Builder
public record InquiryListAdminResponse(

        @Schema(description = "문의 신청 목록")
        List<InquiryReadAdminResponse> responses,

        @Schema(description = "다음 페이지 존재 여부", example = "true | false")
        boolean hasNext,

        @Schema(description = "문의 신청 수", example = "100")
        Long totalCount
) {

}