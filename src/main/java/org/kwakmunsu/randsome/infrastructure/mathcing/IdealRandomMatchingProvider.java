package org.kwakmunsu.randsome.infrastructure.mathcing;

import java.util.List;
import org.kwakmunsu.randsome.admin.matching.service.MatchingProvider;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class IdealRandomMatchingProvider implements MatchingProvider {

    @Override
    public MatchingType getType() {
        return MatchingType.IDEAL_MATCHING;
    }

    @Override
    public List<Member> match(Member requester, int count) {
        return List.of();
    }

}