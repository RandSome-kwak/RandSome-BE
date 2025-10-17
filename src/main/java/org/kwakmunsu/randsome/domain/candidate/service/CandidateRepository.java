package org.kwakmunsu.randsome.domain.candidate.service;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.enums.Gender;

public interface CandidateRepository {

    void save(Candidate candidate);

    Candidate findById(Long id);

    Optional<Candidate> findByMemberId(Long memberId);

    List<Candidate> findRecentApplicationByOrderByCreatedAtDesc(int limit);

    long countByStatus(CandidateStatus candidateStatus);

}