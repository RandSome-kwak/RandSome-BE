package org.kwakmunsu.randsome.domain.matching.repository;

import java.util.List;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingApplicationJpaRepository extends JpaRepository<MatchingApplication, Long> {

    List<MatchingApplication> findAllByRequesterIdAndMatchingStatus(Long requesterId, MatchingStatus status);

    List<MatchingApplication> findAllByRequesterIdAndMatchingStatusIn(Long requesterId, List<MatchingStatus> statuses);
}