package org.kwakmunsu.randsome.domain.candidate.repository;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandidateJpaRepository extends JpaRepository<Candidate, Long> {

    @Query(value = """
                SELECT c FROM Candidate c JOIN FETCH c.member m WHERE m.gender != :gender AND c.status = :status
            """
    )
    List<Candidate> findByGenderAndStatus(
            @Param("gender") Gender gender,
            @Param("status") CandidateStatus status
    );

    Optional<Candidate> findByMemberId(Long memberId);

    long countByStatus(CandidateStatus status);

}