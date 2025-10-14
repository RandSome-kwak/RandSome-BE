package org.kwakmunsu.randsome.domain.matching.repository;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchingApplicationJpaRepository extends JpaRepository<MatchingApplication, Long> {

    List<MatchingApplication> findAllByRequesterIdAndStatus(Long requesterId, MatchingStatus status);

    List<MatchingApplication> findAllByRequesterIdAndStatusIn(Long requesterId, List<MatchingStatus> statuses);

    @Query("SELECT ma FROM MatchingApplication ma " +
            "LEFT JOIN FETCH ma.matchings m " +
            "LEFT JOIN FETCH m.selectedMember " +
            "WHERE ma.id = :id")
    Optional<MatchingApplication> findByIdWithMatchings(@Param("id") Long id);

    long countByStatus(MatchingStatus matchingStatus);
}