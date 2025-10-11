package org.kwakmunsu.randsome.domain.inquiry.serivce.dto;

import lombok.Builder;

@Builder
public record InquiryRegisterServiceRequest(
        Long authorId,
        String title,
        String content
) {

}