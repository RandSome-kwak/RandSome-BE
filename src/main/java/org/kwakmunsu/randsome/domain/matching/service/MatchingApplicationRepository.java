package org.kwakmunsu.randsome.domain.matching.service;

import java.time.LocalDateTime;
import java.util.List;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;

public interface MatchingApplicationRepository {

    MatchingApplication save(MatchingApplication matchingApplication);

    MatchingApplication findById(Long id);

    MatchingApplication findByIdWithMatchings(Long id);

    List<MatchingApplication> findAllByRequesterIdAndMatchingStatus(Long requesterId, MatchingStatus status);

    List<MatchingApplication> findAllByRequesterIdAndMatchingStatusIn(Long requesterId, List<MatchingStatus> statuses);

    List<MatchingApplication> findRecentApplicationByOrderByCreatedAtDesc(int limit);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    long countByMatchingStatus(MatchingStatus matchingStatus);

    long countByRequesterIdAndStatus(Long memberId, EntityStatus status);
}