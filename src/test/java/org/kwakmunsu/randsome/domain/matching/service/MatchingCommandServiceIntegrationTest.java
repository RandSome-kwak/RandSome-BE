package org.kwakmunsu.randsome.domain.matching.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationServiceRequest;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.domain.payment.entity.Payment;
import org.kwakmunsu.randsome.domain.payment.service.PaymentRepository;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
record MatchingCommandServiceIntegrationTest(
        MatchingCommandService matchingCommandService,
        MemberRepository memberRepository,
        MatchingApplicationRepository matchingApplicationRepository,
        PaymentRepository paymentRepository
) {

    @DisplayName("매칭 신청을 한다.")
    @Test
    void applyMatchingApplication() {
        // given
        var requester = MemberFixture.createMember();
        memberRepository.save(requester);
        var request = new MatchingApplicationServiceRequest(requester.getId(), MatchingType.RANDOM_MATCHING, 3);

        // when
        Long matchingApplicationId = matchingCommandService.applyMatching(request);

        // then
        var matchingApplication = matchingApplicationRepository.findById(matchingApplicationId);
        assertThat(matchingApplication).extracting(
                        MatchingApplication::getId,
                        MatchingApplication::getRequester,
                        MatchingApplication::getMatchingType,
                        MatchingApplication::getStatus,
                        MatchingApplication::getRequestedCount
                )
                .containsExactly(
                        matchingApplicationId,
                        requester,
                        request.type(),
                        MatchingStatus.PENDING,
                        request.matchingCount()
                );
        List<Payment> payments = paymentRepository.findByMemberId(request.memberId());

        assertThat(payments).hasSize(1);
        assertThat(payments.getFirst().getMember().getId()).isEqualTo(requester.getId());
    }

}