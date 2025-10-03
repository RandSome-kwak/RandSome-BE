package org.kwakmunsu.randsome.admin.matching;

import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;

public interface MatchingProvider {

    MatchingType getType();
    void match(Long memberId);

}
