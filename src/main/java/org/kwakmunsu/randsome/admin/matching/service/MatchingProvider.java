package org.kwakmunsu.randsome.admin.matching.service;

import java.util.List;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.member.entity.Member;

public interface MatchingProvider {

    MatchingType getType();
    List<Member> match(Member requester);

}
