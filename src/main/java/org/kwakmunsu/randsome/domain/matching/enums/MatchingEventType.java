package org.kwakmunsu.randsome.domain.matching.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchingEventType {

    MATCHING ("매칭 신청"),
    CANDIDATE("후보 등록"),
    ;

    private final String description;

}