package org.kwakmunsu.randsome.domain.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {
    CANDIDATE_REGISTRATION (1000, "매칭 후보자 등록"),
    RANDOM_MATCHING        (1000, "랜덤 매칭"),
    IDEAL_MATCHING         (1500, "이상형 랜덤 매칭"),
    ;

    private final int amount;
    private final String description;

}