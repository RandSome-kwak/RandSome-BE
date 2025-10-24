package org.kwakmunsu.randsome.domain.matching.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingRepository {

    private final MatchingJpaRepository matchingJpaRepository;

    public long countBySelectedMemberIdAndStatus(Long selectedMemberId, EntityStatus status) {
        return matchingJpaRepository.countBySelectedMemberIdAndStatus(selectedMemberId, status);
    }

}