package org.kwakmunsu.randsome.domain.matching.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kwakmunsu.randsome.domain.payment.enums.PaymentType;

@Getter
@AllArgsConstructor
public enum MatchingType {

    RANDOM_MATCHING ("랜덤"),
    IDEAL_MATCHING  ("이상형"),
    ;

    private final String description;

    public PaymentType toPaymentType() {
        return switch (this) {
            case RANDOM_MATCHING -> PaymentType.RANDOM_MATCHING;
            case IDEAL_MATCHING  -> PaymentType.IDEAL_MATCHING;
        };
    }

}