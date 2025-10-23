package org.kwakmunsu.randsome.domain.matching.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.matching.service.MatchingRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingRepositoryImpl implements MatchingRepository {

    private final MatchingJpaRepository matchingJpaRepository;

    @Override
    public long countBySelectedMemberIdAndStatus(Long selectedMemberId, EntityStatus status) {
        return matchingJpaRepository.countBySelectedMemberIdAndStatus(selectedMemberId, status);
    }

}