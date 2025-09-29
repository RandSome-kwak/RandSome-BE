package org.kwakmunsu.randsome.domain.payment.repository;

import java.util.List;
import org.kwakmunsu.randsome.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByMemberId(Long memberId);

}