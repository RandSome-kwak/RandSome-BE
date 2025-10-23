package org.kwakmunsu.randsome.domain.candidate.repository;

import java.util.Optional;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateJpaRepository extends JpaRepository<Candidate, Long> {

    Optional<Candidate> findByMemberId(Long memberId);

    long countByCandidateStatusAndStatus(CandidateStatus candidateStatus, EntityStatus status);

}