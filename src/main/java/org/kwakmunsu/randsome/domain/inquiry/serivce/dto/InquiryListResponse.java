package org.kwakmunsu.randsome.domain.inquiry.serivce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "문의 리스트 응답 DTO")
public record InquiryListResponse(List<InquiryReadResponse> inquiries) {

}