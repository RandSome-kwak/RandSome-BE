package org.kwakmunsu.randsome.domain.payment.serivce;

import java.util.List;
import org.kwakmunsu.randsome.domain.payment.entity.Payment;

public interface PaymentRepository {

    void save(Payment payment);
    Payment findById(Long id);
    List<Payment> findByMemberId(Long memberId);

}