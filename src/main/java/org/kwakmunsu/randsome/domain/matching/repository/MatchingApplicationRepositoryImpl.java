package org.kwakmunsu.randsome.domain.matching.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.dto.AdminMatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingApplicationRepositoryImpl implements MatchingApplicationRepository {

    private final MatchingApplicationJpaRepository matchingApplicationJpaRepository;
    private final MatchingApplicationQueryDslRepository matchingApplicationQueryDslRepository;

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
    public List<MatchingApplication> findAllByRequesterIdAndStatus(Long requesterId, MatchingStatus status) {
        return matchingApplicationJpaRepository.findAllByRequesterIdAndStatus(requesterId, status);
    }

    @Override
    public List<MatchingApplication> findAllByRequesterIdAndStatusIn(Long requesterId, List<MatchingStatus> statuses) {
        return matchingApplicationJpaRepository.findAllByRequesterIdAndStatusIn(requesterId, statuses);
    }

    @Override
    public List<MatchingApplication> findRecentApplicationByOrderByCreatedAtDesc(int limit) {
        String jpql = "SELECT m FROM MatchingApplication m JOIN FETCH m.requester ORDER BY m.createdAt DESC";

        return entityManager.createQuery(jpql, MatchingApplication.class)
                .setMaxResults(limit)
                .getResultList();
    }

    // Admin 전용 메서드
    @Override
    public AdminMatchingApplicationListResponse findAllByStatus(MatchingStatus status, int page) {
        return matchingApplicationQueryDslRepository.findAllByStatus(status, page);
    }

    @Override
    public long countByStatus(MatchingStatus matchingStatus) {
        return matchingApplicationJpaRepository.countByStatus(matchingStatus);
    }

}