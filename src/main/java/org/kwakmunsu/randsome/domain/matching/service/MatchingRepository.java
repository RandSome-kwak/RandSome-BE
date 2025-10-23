package org.kwakmunsu.randsome.domain.matching.service;

import org.kwakmunsu.randsome.domain.EntityStatus;

public interface MatchingRepository {

    long countBySelectedMemberIdAndStatus(Long selectedMemberId, EntityStatus status);

}