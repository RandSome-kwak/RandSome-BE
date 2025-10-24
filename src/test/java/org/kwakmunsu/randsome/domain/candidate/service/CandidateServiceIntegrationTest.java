package org.kwakmunsu.randsome.domain.candidate.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.candidate.repository.CandidateRepository;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.repository.MemberRepository;
import org.kwakmunsu.randsome.domain.payment.entity.Payment;
import org.kwakmunsu.randsome.domain.payment.repository.PaymentRepository;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
record CandidateServiceIntegrationTest(
        CandidateService candidateService,
        CandidateRepository candidateRepository,
        MemberRepository memberRepository,
        PaymentRepository paymentRepository
) {

    @DisplayName("후보자 등록 성공 후 결제 정보가 등록된다.")
    @Test
    void candidateRegisterCreatesPayment() {
        var member = MemberFixture.createMember();
        memberRepository.save(member);

        // when
        candidateService.register(member.getId());

        // then
        List<Payment> payments = paymentRepository.findByMemberId(member.getId());

        assertThat(payments).hasSize(1);
        assertThat(payments.getFirst().getMember().getId()).isEqualTo(member.getId());
    }

    @DisplayName("후보자 등록 성공 후 결제 정보가 등록이 안됐다면 롤백 된다.")
    @Test
    void candidateRegisterPaymentFailRollsBack() {
        // FIXME: 테스트 방법을 모르곘음
    }

}
