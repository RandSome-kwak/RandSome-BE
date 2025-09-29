package org.kwakmunsu.randsome.domain.candidate.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.serivce.CandidateRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CandidateRepositoryImpl implements CandidateRepository {

    private final CandidateJpaRepository candidateJpaRepository;

    @Override
    public void save(Candidate candidate) {
        candidateJpaRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> findByMemberId(Long memberId) {
        return candidateJpaRepository.findByMemberId(memberId);
    }

}