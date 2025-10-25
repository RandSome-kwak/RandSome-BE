package org.kwakmunsu.randsome.domain.matching.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingApplicationRepository {

    private final MatchingApplicationJpaRepository matchingApplicationJpaRepository;
    private final MatchingApplicationQueryDslRepository matchingApplicationQueryDslRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MatchingApplication save(MatchingApplication matchingApplication) {
        return matchingApplicationJpaRepository.save(matchingApplication);
    }

    public MatchingApplication findById(Long id) {
        return matchingApplicationJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MATCHING_APPLICATION));
    }

    public MatchingApplication findByIdWithMatchings(Long id) {
        return matchingApplicationJpaRepository.findByIdWithMatchings(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MATCHING_APPLICATION));
    }

    public List<MatchingApplication> findAllByRequesterIdAndMatchingStatusIn(Long requesterId, int limit, Long lastApplicationId,  List<MatchingStatus> statuses) {
        return matchingApplicationQueryDslRepository.findAllByRequesterIdAndMatchingStatusIn(requesterId, statuses, limit, lastApplicationId);
    }

    public List<MatchingApplication> findRecentApplicationByOrderByCreatedAtDesc(int limit) {
        String jpql = "SELECT m FROM MatchingApplication m JOIN FETCH m.requester ORDER BY m.createdAt DESC";

        return entityManager.createQuery(jpql, MatchingApplication.class)
                .setMaxResults(limit)
                .getResultList();
    }

    public long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return matchingApplicationJpaRepository.countByCreatedAtBetween(start, end);
    }

    public long countByMatchingStatus(MatchingStatus matchingStatus) {
        return matchingApplicationJpaRepository.countByMatchingStatus(matchingStatus);
    }

    public long countByRequesterIdAndStatus(Long requestId, EntityStatus status) {
        return matchingApplicationJpaRepository.countByRequesterIdAndStatus(requestId, status);
    }

}