package org.kwakmunsu.randsome.domain.member.repository;

import static com.querydsl.core.types.Projections.constructor;
import static org.kwakmunsu.randsome.domain.member.entity.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberListResponse;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberPreviewResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberQueryDslRepository {

    private static final int PAGE_SIZE = 20;
    private static final int NEXT_PAGE_CHECK_SIZE = 1;
    private final JPAQueryFactory queryFactory;

    public MemberListResponse findAll(int page) {
        int offset = (page - 1) * PAGE_SIZE;
        int limit = PAGE_SIZE + NEXT_PAGE_CHECK_SIZE; // 다음 페이지 존재 여부 체크용

        List<MemberPreviewResponse> responses = queryFactory.select(
                        constructor(
                                MemberPreviewResponse.class,
                                member.id,
                                member.loginId,
                                member.legalName,
                                member.nickname,
                                member.gender,
                                member.role,
                                member.createdAt
                        ))
                .from(member)
                .orderBy(member.createdAt.desc())
                .offset(offset)
                .limit(limit)
                .fetch();

        boolean hasNext = responses.size() > PAGE_SIZE;
        List<MemberPreviewResponse> limitedPage = getLimitedPage(responses, hasNext);
        Long totalCount = countAll();

        return new MemberListResponse(limitedPage, hasNext, totalCount);
    }

    public Long countAll() {
        return queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();
    }

    private List<MemberPreviewResponse> getLimitedPage(List<MemberPreviewResponse> responses, boolean hasNext) {
        if (hasNext) return responses.subList(0, PAGE_SIZE); // 실제로는 limit 만큼만 반환
        return responses;
    }
}
