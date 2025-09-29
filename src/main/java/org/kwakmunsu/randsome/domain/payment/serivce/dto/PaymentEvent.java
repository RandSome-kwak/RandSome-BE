package org.kwakmunsu.randsome.domain.payment.serivce.dto;

import lombok.Builder;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.payment.enums.PaymentType;

@Builder
public record PaymentEvent(
        Member member,
        PaymentType paymentType,
        int count
) {
    public PaymentEvent(Member member, PaymentType paymentType) {
        this(member, paymentType, 1);
    }

}