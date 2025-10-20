package org.kwakmunsu.randsome.admin.candidate.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.candidate.serivce.CandidateAdminRepository;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CandidateAdminRepositoryImpl implements CandidateAdminRepository {

    private final CandidateAdminJpaRepository candidateJpaRepository;
    private final CandidateQueryDslRepository candidateQueryDslRepository;

    @Override
    public List<Candidate> findAllByCandidateStatus(CandidateStatus status, int offset, int limit) {
        return candidateQueryDslRepository.findAllByCandidateStatus(status, offset, limit);
    }

    @Override
    public Candidate findByIdWithMember(Long id) {
        return candidateJpaRepository.findByIdWithMember(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_CANDIDATE));
    }

    @Override
    public List<Candidate> findByGenderAndCandidateStatus(Gender gender, CandidateStatus status) {
        return candidateJpaRepository.findByGenderAndStatus(gender, status);
    }


    @Override
    public long countByCandidateStatusAndStatus(CandidateStatus candidateStatus, EntityStatus status) {
        return candidateJpaRepository.countByCandidateStatusAndStatus(candidateStatus, status);
    }

}
