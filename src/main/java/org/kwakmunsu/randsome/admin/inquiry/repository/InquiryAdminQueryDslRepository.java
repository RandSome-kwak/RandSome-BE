package org.kwakmunsu.randsome.admin.inquiry.repository;

import static org.kwakmunsu.randsome.domain.inquiry.entity.QInquiry.inquiry;
import static org.kwakmunsu.randsome.domain.member.entity.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryAdminQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Inquiry> findAllByInquiryStatusAndStatus(InquiryStatus inquiryStatus, int offset, int limit, EntityStatus status) {

        return queryFactory.selectFrom(inquiry)
                .join(inquiry.author, member)
                .where(
                        inquiryStatusEq(inquiryStatus),
                        isActive(status)
                )
                .orderBy(inquiry.id.desc())
                .offset(offset)
                .limit(limit) // 다음 페이지 존재 여부 체크용
                .fetch();
    }

    private BooleanExpression isActive(EntityStatus status) {
        return inquiry.status.eq(status);
    }

    private BooleanExpression inquiryStatusEq(InquiryStatus status) {
        return status == null ? inquiry.inquiryStatus.eq(InquiryStatus.PENDING) : inquiry.inquiryStatus.eq(status);
    }

}