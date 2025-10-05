package org.kwakmunsu.randsome.admin.matching.service;

import java.util.List;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class RandomMatchingProvider implements MatchingProvider {

    @Override
    public MatchingType getType() {
        return MatchingType.RANDOM_MATCHING;
    }

    @Override
    public List<Member> match(Member requester) {
        return List.of();
    }

}