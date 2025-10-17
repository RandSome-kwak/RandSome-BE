package org.kwakmunsu.randsome.admin.candidate.serivce;

import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;

public interface CandidateAdminRepository {

    CandidateListResponse findAllByStatus(CandidateStatus status, int page);

    Candidate findByIdWithMember(Long id);

    long countByStatus(CandidateStatus candidateStatus);

}