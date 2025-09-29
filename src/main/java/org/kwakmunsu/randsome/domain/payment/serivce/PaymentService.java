package org.kwakmunsu.randsome.domain.payment.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.payment.entity.Payment;
import org.kwakmunsu.randsome.domain.payment.serivce.dto.PaymentEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // 한 트랜잭션으로 묶여야함.
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void create(PaymentEvent event) {
        Payment payment = createPaymentByType(event);
        paymentRepository.save(payment);
        log.info("[new PaymentCreated] paymentId= {}, memberName={}, paymentType= {}, price = {}", payment.getId(), event.member().getLegalName(), payment.getType().getDescription(), payment.getAmount());
    }

    private Payment createPaymentByType(PaymentEvent paymentEvent) {
        return switch (paymentEvent.paymentType()) {
            case CANDIDATE_REGISTRATION -> Payment.createCandidateRegistration(paymentEvent.member());
            case RANDOM_MATCHING -> Payment.createRandomMatching(paymentEvent.member(), paymentEvent.count());
            case IDEAL_MATCHING -> Payment.createIdealMatching(paymentEvent.member(), paymentEvent.count());
        };
    }

}