package org.kwakmunsu.randsome.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    M     ("남자"),
    F     ("여자"),
    ADMIN ("관리자")
    ;

    private final String value;

}