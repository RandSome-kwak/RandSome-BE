package org.kwakmunsu.randsome.admin.candidate.repository;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandidateAdminJpaRepository extends JpaRepository<Candidate, Long> {

    @Query(value = """
                SELECT c FROM Candidate c JOIN FETCH c.member m WHERE m.gender != :gender AND c.candidateStatus = :candidateStatus AND c.status = :status
            """
    )
    List<Candidate> findByGenderAndCandidateStatusAndStatus(
            @Param("gender") Gender gender,
            @Param("candidateStatus") CandidateStatus candidateStatus,
            @Param("status") EntityStatus status
    );

    @Query("SELECT c FROM Candidate c JOIN FETCH c.member WHERE c.id = :id AND c.status = :status")
    Optional<Candidate> findByIdWithMemberAndStatus(@Param("id") Long id, @Param("status") EntityStatus status);

    long countByCandidateStatusAndStatus(CandidateStatus candidateStatus, EntityStatus status);

}