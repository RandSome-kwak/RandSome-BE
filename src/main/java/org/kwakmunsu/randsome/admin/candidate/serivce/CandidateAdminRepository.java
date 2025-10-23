package org.kwakmunsu.randsome.admin.candidate.serivce;

import java.util.List;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.enums.Gender;

public interface CandidateAdminRepository {

    List<Candidate> findAllByCandidateStatus(CandidateStatus status, int offset, int limit);

    Candidate findByIdWithMemberAndStatus(Long id, EntityStatus status);

    List<Candidate> findByGenderAndCandidateStatusAndStatus(Gender gender, CandidateStatus candidateStatus, EntityStatus status);

    long countByCandidateStatusAndStatus(CandidateStatus candidateStatus, EntityStatus status);

}