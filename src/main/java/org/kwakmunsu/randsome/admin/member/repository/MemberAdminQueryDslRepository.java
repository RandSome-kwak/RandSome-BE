package org.kwakmunsu.randsome.admin.member.repository;

import static com.querydsl.core.types.Projections.constructor;
import static org.kwakmunsu.randsome.domain.member.entity.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.member.repository.dto.MemberListResponse;
import org.kwakmunsu.randsome.admin.member.repository.dto.MemberPreviewResponse;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberAdminQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Member> findAllByStatus(int offset, int limit, EntityStatus status) {
        return queryFactory.selectFrom(member)
                .where(statusEq(status))
                .orderBy(member.id.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    private BooleanExpression statusEq(EntityStatus status) {
        return member.status.eq(status);
    }

}