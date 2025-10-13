package org.kwakmunsu.randsome.domain.candidate.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.domain.candidate.serivce.CandidateRepository;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CandidateRepositoryImpl implements CandidateRepository {

    private final CandidateJpaRepository candidateJpaRepository;
    private final CandidateQueryDslRepository candidateQueryDslRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public List<Candidate> findRecentApplicationByOrderByCreatedAtDesc(int limit) {
        String jpql = "SELECT c FROM Candidate c JOIN FETCH c.member ORDER BY c.createdAt DESC";

        return entityManager.createQuery(jpql, Candidate.class)
                .setMaxResults(limit)
                .getResultList();
    }

    // Admin 전용 메서드
    @Override
    public CandidateListResponse findAllByStatus(CandidateStatus status, int page) {
        return candidateQueryDslRepository.findAllByStatus(status, page);
    }

    @Override
    public List<Candidate> findByGenderAndStatus(Gender gender, CandidateStatus status) {
        return candidateJpaRepository.findByGenderAndStatus(gender, status);
    }

}