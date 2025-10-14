package org.kwakmunsu.randsome.domain.inquiry.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema
@Builder
public record InquiryUpdateServiceRequest(
        Long inquiryId,
        Long authorId,
        String title,
        String content
) {

}