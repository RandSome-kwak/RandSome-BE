package org.kwakmunsu.randsome.domain.matching.enums;

import org.kwakmunsu.randsome.domain.payment.enums.PaymentType;

public enum MatchingType {
    RANDOM_MATCHING,
    IDEAL_MATCHING
    ;

    public PaymentType toPaymentType() {
        return switch (this) {
            case RANDOM_MATCHING -> PaymentType.RANDOM_MATCHING;
            case IDEAL_MATCHING  -> PaymentType.IDEAL_MATCHING;
        };
    }

}