package org.kwakmunsu.randsome.domain.candidate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CandidateStatus {

    PENDING  ("대기"),
    APPROVED ("승인"),
    REJECTED ("거절"),
    ;

    private final String description;

}