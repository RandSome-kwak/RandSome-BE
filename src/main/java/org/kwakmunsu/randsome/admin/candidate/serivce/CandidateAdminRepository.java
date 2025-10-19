package org.kwakmunsu.randsome.admin.candidate.serivce;

import java.util.List;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.enums.Gender;

public interface CandidateAdminRepository {

    CandidateListResponse findAllByCandidateStatus(CandidateStatus status, int page);

    Candidate findByIdWithMember(Long id);

    List<Candidate> findByGenderAndCandidateStatus(Gender gender, CandidateStatus status);

    long countByCandidateStatus(CandidateStatus candidateStatus);

}