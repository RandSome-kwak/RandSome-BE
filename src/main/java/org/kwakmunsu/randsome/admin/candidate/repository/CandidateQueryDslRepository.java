package org.kwakmunsu.randsome.admin.candidate.repository;

import static org.kwakmunsu.randsome.domain.candidate.entity.QCandidate.candidate;
import static org.kwakmunsu.randsome.domain.member.entity.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CandidateQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Candidate> findAllByCandidateStatus(CandidateStatus status, int offset, int limit) {
        return queryFactory
                .selectFrom(candidate)
                .join(candidate.member, member).fetchJoin()
                .where(
                        candidateStatusEq(status),
                        isActive()
                )
                .orderBy(candidate.id.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    private BooleanExpression candidateStatusEq(CandidateStatus status) {
        // 기본값 : PENDING
        CandidateStatus candidateStatus = status != null ? status : CandidateStatus.PENDING;
        return candidate.candidateStatus.eq(candidateStatus);
    }

    private BooleanExpression isActive() {
        return candidate.status.eq(EntityStatus.ACTIVE);
    }

}