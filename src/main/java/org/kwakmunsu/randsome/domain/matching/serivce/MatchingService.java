package org.kwakmunsu.randsome.domain.matching.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.serivce.dto.MatchingApplicationServiceRequest;
import org.kwakmunsu.randsome.domain.matching.serivce.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.kwakmunsu.randsome.domain.payment.serivce.dto.PaymentEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchingService {

    private final MemberRepository memberRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long matchingApply(MatchingApplicationServiceRequest request) {
        MatchingType matchingType = request.type();
        Member member = memberRepository.findById(request.memberId());

        MatchingApplication matchingApplication = MatchingApplication.create(member, matchingType, request.matchingCount());
        MatchingApplication application = matchingApplicationRepository.save(matchingApplication);
        log.info("[new MatchingApplication]. MatchingApplicationId = {}, memberId = {}", application.getId(), member.getId());

        eventPublisher.publishEvent(new PaymentEvent(member, matchingType.toPaymentType(), request.matchingCount()));

        return application.getId();
    }

}