package org.kwakmunsu.randsome.domain.matching.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchingStatus {

    PENDING   ("대기"),
    COMPLETED ("완료"),
    FAILED    ("실패"),
    ;

    private final String description;

}