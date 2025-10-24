package org.kwakmunsu.randsome.domain.payment.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.payment.entity.Payment;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public void save(Payment payment) {
        paymentJpaRepository.save(payment);
    }

    public Payment findById(Long id) {
        return paymentJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_PAYMENT));
    }

    public List<Payment> findByMemberId(Long memberId) {
        return paymentJpaRepository.findByMemberId(memberId);
    }

}