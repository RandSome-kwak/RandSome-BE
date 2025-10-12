package org.kwakmunsu.randsome.domain.inquiry.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryState {

    PENDING  ("대기"),
    COMPLETED("완료"),
    ;

    private final String description;

}