package org.kwakmunsu.randsome.global.jwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {

    AUTHORIZATION_HEADER ("Authorization"),
    BEARER_PREFIX        ("Bearer "),
    ACCESS               ("accessToken"),
    REFRESH              ("refreshToken"),
    ;

    private final String value;

}