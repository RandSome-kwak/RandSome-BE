package org.kwakmunsu.randsome.infrastructure.mathcing;

import org.kwakmunsu.randsome.admin.matching.service.MatchingProvider;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.springframework.stereotype.Component;

@Component
public class IdealRandomMatchingProvider implements MatchingProvider {

    @Override
    public MatchingType getType() {
        return MatchingType.IDEAL_MATCHING;
    }

    @Override
    public void match(Long memberId) {

    }

}