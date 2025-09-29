package org.kwakmunsu.randsome.domain.candidate.serivce;

import java.util.Optional;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;

public interface CandidateRepository {

    void save(Candidate candidate);
    Candidate findById(Long id);
    Optional<Candidate> findByMemberId(Long memberId);

}