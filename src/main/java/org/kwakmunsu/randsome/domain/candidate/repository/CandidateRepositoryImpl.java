package org.kwakmunsu.randsome.domain.candidate.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.domain.candidate.serivce.CandidateRepository;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CandidateRepositoryImpl implements CandidateRepository {

    private final CandidateJpaRepository candidateJpaRepository;
    private final CandidateQueryDslRepository candidateQueryDslRepository;

    @Override
    public void save(Candidate candidate) {
        candidateJpaRepository.save(candidate);
    }

    @Override
    public Candidate findById(Long id) {
        return candidateJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_CANDIDATE));
    }

    @Override
    public Candidate findByIdWithMember(Long id) {
        return candidateJpaRepository.findByIdWithMember(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_CANDIDATE));
    }

    @Override
    public Optional<Candidate> findByMemberId(Long memberId) {
        return candidateJpaRepository.findByMemberId(memberId);
    }

    // Admin 전용 메서드
    @Override
    public CandidateListResponse findAllByStatus(CandidateStatus status, int page) {
        return candidateQueryDslRepository.findAllByStatus(status, page);
    }

}