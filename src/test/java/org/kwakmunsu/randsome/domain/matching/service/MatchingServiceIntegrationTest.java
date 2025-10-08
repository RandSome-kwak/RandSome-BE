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
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.kwakmunsu.randsome.domain.payment.entity.Payment;
import org.kwakmunsu.randsome.domain.payment.serivce.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MatchingServiceIntegrationTest {

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MatchingApplicationRepository matchingApplicationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // FIXME : transactional 미적용 시 DB 제약 조건 위반으로 테스트 실패. 그러나 트랜잭션을 헤제해야 테스트 진행됨
    @DisplayName("매칭 신청을 한다.")
    @Test
    void matchingApplyApplication() {
        // given
        var requester = MemberFixture.createMember();
        memberRepository.save(requester);
        var request = new MatchingApplicationServiceRequest(requester.getId(), MatchingType.RANDOM_MATCHING, 3);

        // when
        Long matchingApplicationId = matchingService.matchingApply(request);

        // then
        var matchingApplication = matchingApplicationRepository.findById(matchingApplicationId);
        assertThat(matchingApplication).extracting(
                        MatchingApplication::getId,
                        MatchingApplication::getRequester,
                        MatchingApplication::getMatchingType,
                        MatchingApplication::getMatchingStatus,
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