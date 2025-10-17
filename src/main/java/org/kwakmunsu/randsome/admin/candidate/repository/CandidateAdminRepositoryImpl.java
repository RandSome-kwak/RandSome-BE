package org.kwakmunsu.randsome.admin.candidate.repository;

import org.kwakmunsu.randsome.admin.candidate.serivce.CandidateAdminRepository;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CandidateAdminRepositoryImpl implements CandidateAdminRepository {

    private final CandidateAdminJpaRepository candidateJpaRepository;
    private final CandidateQueryDslRepository candidateQueryDslRepository;

    @Override
    public CandidateListResponse findAllByStatus(CandidateStatus status, int page) {
        return candidateQueryDslRepository.findAllByStatus(status, page);
    }

    @Override
    public Candidate findByIdWithMember(Long id) {
        return candidateJpaRepository.findByIdWithMember(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_CANDIDATE));
    }

    @Override
    public long countByStatus(CandidateStatus candidateStatus) {
        return candidateJpaRepository.countByStatus(candidateStatus);
    }

}
