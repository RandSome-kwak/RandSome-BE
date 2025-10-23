package org.kwakmunsu.randsome.domain.matching.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.service.MatchingApplicationRepository;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingApplicationRepositoryImpl implements MatchingApplicationRepository {

    private final MatchingApplicationJpaRepository matchingApplicationJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MatchingApplication save(MatchingApplication matchingApplication) {
        return matchingApplicationJpaRepository.save(matchingApplication);
    }

    @Override
    public MatchingApplication findById(Long id) {
        return matchingApplicationJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MATCHING_APPLICATION));
    }

    @Override
    public MatchingApplication findByIdWithMatchings(Long id) {
        return matchingApplicationJpaRepository.findByIdWithMatchings(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MATCHING_APPLICATION));
    }

    @Override
    public List<MatchingApplication> findAllByRequesterIdAndMatchingStatus(Long requesterId, MatchingStatus status) {
        return matchingApplicationJpaRepository.findAllByRequesterIdAndMatchingStatus(requesterId, status);
    }

    @Override
    public List<MatchingApplication> findAllByRequesterIdAndMatchingStatusIn(Long requesterId, List<MatchingStatus> statuses) {
        return matchingApplicationJpaRepository.findAllByRequesterIdAndMatchingStatusIn(requesterId, statuses);
    }

    @Override
    public List<MatchingApplication> findRecentApplicationByOrderByCreatedAtDesc(int limit) {
        String jpql = "SELECT m FROM MatchingApplication m JOIN FETCH m.requester ORDER BY m.createdAt DESC";

        return entityManager.createQuery(jpql, MatchingApplication.class)
                .setMaxResults(limit)
                .getResultList();
    }

    public long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return matchingApplicationJpaRepository.countByCreatedAtBetween(start, end);
    }

    @Override
    public long countByMatchingStatus(MatchingStatus matchingStatus) {
        return matchingApplicationJpaRepository.countByMatchingStatus(matchingStatus);
    }

    @Override
    public long countByRequesterIdAndStatus(Long requestId, EntityStatus status) {
        return matchingApplicationJpaRepository.countByRequesterIdAndStatus(requestId, status);
    }

}