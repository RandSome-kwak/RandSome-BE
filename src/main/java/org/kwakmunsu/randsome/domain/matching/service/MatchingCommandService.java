package org.kwakmunsu.randsome.domain.matching.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationServiceRequest;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.domain.payment.service.dto.PaymentEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchingCommandService {

    private final MemberRepository memberRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long applyMatching(MatchingApplicationServiceRequest request) {
        MatchingType matchingType = request.type();
        Member member = memberRepository.findById(request.memberId());

        MatchingApplication application = createAndSaveApplication(member, matchingType, request.matchingCount());

        log.info("[new MatchingApplication]. MatchingApplicationId = {}, memberId = {}", application.getId(), member.getId());

        eventPublisher.publishEvent(new PaymentEvent(member, matchingType.toPaymentType(), request.matchingCount()));

        return application.getId();
    }

    private MatchingApplication createAndSaveApplication(Member member, MatchingType matchingType, int matchingCount) {
        MatchingApplication matchingApplication = MatchingApplication.create(member, matchingType, matchingCount);
        return matchingApplicationRepository.save(matchingApplication);
    }

}