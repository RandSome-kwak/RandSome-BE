package org.kwakmunsu.randsome.domain.candidate.repository;

import java.util.Optional;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandidateJpaRepository extends JpaRepository<Candidate, Long> {

    Optional<Candidate> findByMemberId(Long memberId);

    @Query("SELECT c FROM Candidate c JOIN FETCH c.member WHERE c.id = :id")
    Optional<Candidate> findByIdWithMember(@Param("id") Long id);

}