package org.kwakmunsu.randsome.domain.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.randsome.domain.BaseEntity;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.payment.enums.PaymentStatus;
import org.kwakmunsu.randsome.domain.payment.enums.PaymentType;

@Table(name = "payments")
@Builder(access =  AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentType type;

    private static Payment createPayment(Member member, PaymentType type, int matchingCount) {
        int totalPrice = type.getAmount() * matchingCount;

        return Payment.builder()
                .member(member)
                .amount(BigDecimal.valueOf(totalPrice))
                .paymentStatus(PaymentStatus.PENDING)
                .type(type)
                .build();
    }

    public static Payment createCandidateRegistration(Member member) {
        return createPayment(member, PaymentType.CANDIDATE_REGISTRATION, 1);
    }

    public static Payment createRandomMatching(Member member, int count) {
        return createPayment(member, PaymentType.RANDOM_MATCHING, count);
    }

    public static Payment createIdealMatching(Member member, int count) {
        return createPayment(member, PaymentType.IDEAL_MATCHING, count);
    }

    public void complete() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void fail() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

}