package org.kwakmunsu.randsome.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_GUEST     ("손님"),
    ROLE_MEMBER    ("회원"),
    ROLE_CANDIDATE ("매칭 후보"),
    ROLE_ADMIN     ("관리자"),
    ;

    private final String value;

}