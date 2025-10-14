package org.kwakmunsu.randsome.domain.candidate.serivce;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.domain.member.enums.Gender;

public interface CandidateRepository {

    void save(Candidate candidate);
    Candidate findById(Long id);
    Candidate findByIdWithMember(Long id);
    Optional<Candidate> findByMemberId(Long memberId);
    List<Candidate> findRecentApplicationByOrderByCreatedAtDesc(int limit);

    // Admin 전용 메서드
    CandidateListResponse findAllByStatus(CandidateStatus status, int page);
    List<Candidate> findByGenderAndStatus(Gender gender, CandidateStatus status);
    long countByStatus(CandidateStatus candidateStatus);
}