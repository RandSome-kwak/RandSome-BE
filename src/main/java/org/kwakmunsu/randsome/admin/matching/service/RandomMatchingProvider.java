package org.kwakmunsu.randsome.admin.matching.service;

import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.springframework.stereotype.Component;

@Component
public class RandomMatchingProvider implements MatchingProvider {

    @Override
    public MatchingType getType() {
        return MatchingType.RANDOM_MATCHING;
    }

    @Override
    public void match(Long memberId) {

    }

}