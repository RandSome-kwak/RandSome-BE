package org.kwakmunsu.randsome.domain.candidate.service;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;

public interface CandidateRepository {

    void save(Candidate candidate);

    Candidate findById(Long id);

    Optional<Candidate> findByMemberId(Long memberId);

    List<Candidate> findRecentApplicationByOrderByCreatedAtDesc(int limit);

    long countByCandidateStatusAndStatus(CandidateStatus candidateStatus, EntityStatus status);

}