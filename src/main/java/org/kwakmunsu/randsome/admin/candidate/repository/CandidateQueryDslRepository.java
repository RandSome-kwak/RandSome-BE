package org.kwakmunsu.randsome.admin.candidate.repository;

import static com.querydsl.core.types.Projections.constructor;
import static org.kwakmunsu.randsome.domain.candidate.entity.QCandidate.candidate;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidatePreviewResponse;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CandidateQueryDslRepository {

    private static final int PAGE_SIZE = 20;
    private static final int NEXT_PAGE_CHECK_SIZE = 1;
    private final JPAQueryFactory queryFactory;

    public CandidateListResponse findAllByStatus(CandidateStatus status, int page) {
        int offset = (page - 1) * PAGE_SIZE;
        int limit = PAGE_SIZE + NEXT_PAGE_CHECK_SIZE; // 다음 페이지 존재 여부 체크용

        List<CandidatePreviewResponse> responses = queryFactory.select(
                        constructor(
                                CandidatePreviewResponse.class,
                                candidate.id,
                                candidate.member.id,
                                candidate.member.legalName,
                                candidate.member.nickname,
                                candidate.member.gender,
                                candidate.createdAt,
                                candidate.candidateStatus
                        ))
                .from(candidate)
                .where(
                        statusEq(status)
                )
                .orderBy(candidate.createdAt.desc())
                .offset(offset)
                .limit(limit)
                .fetch();

        boolean hasNext = responses.size() > PAGE_SIZE;
        List<CandidatePreviewResponse> limitedPage = getLimitedPage(responses, hasNext);
        Long totalCount = countByStatus(status);

        return new CandidateListResponse(limitedPage, hasNext, totalCount);
    }

    public Long countByStatus(CandidateStatus status) {
        return queryFactory
                .select(candidate.count())
                .from(candidate)
                .where(statusEq(status))
                .fetchOne();
    }

    private BooleanExpression statusEq(CandidateStatus status) {
        // 기본값 : PENDING
        CandidateStatus candidateStatus = status != null ? status : CandidateStatus.PENDING;
        return candidate.candidateStatus.eq(candidateStatus);
    }

    private List<CandidatePreviewResponse> getLimitedPage(List<CandidatePreviewResponse> responses, boolean hasNext) {
        if (hasNext) return responses.subList(0, PAGE_SIZE); // 실제로는 limit 만큼만 반환
        return responses;
    }

}