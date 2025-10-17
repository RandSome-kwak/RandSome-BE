package org.kwakmunsu.randsome.admin.candidate.repository;

import java.util.Optional;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandidateAdminJpaRepository extends JpaRepository<Candidate, Long> {

    @Query("SELECT c FROM Candidate c JOIN FETCH c.member WHERE c.id = :id")
    Optional<Candidate> findByIdWithMember(@Param("id") Long id);

    long countByStatus(CandidateStatus status);

}