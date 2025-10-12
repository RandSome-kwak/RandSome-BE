package org.kwakmunsu.randsome.domain.inquiry.repository;

import static com.querydsl.core.types.Projections.constructor;
import static org.kwakmunsu.randsome.domain.inquiry.entity.QInquiry.inquiry;
import static org.kwakmunsu.randsome.domain.member.entity.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryState;
import org.kwakmunsu.randsome.domain.inquiry.repository.dto.InquiryListAdminResponse;
import org.kwakmunsu.randsome.domain.inquiry.repository.dto.InquiryReadAdminResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryQueryDslRepository {

    private static final int PAGE_SIZE = 20;
    private static final int NEXT_PAGE_CHECK_SIZE = 1;
    private final JPAQueryFactory queryFactory;

    public InquiryListAdminResponse findAllByState(InquiryState state, int page) {
        int offset = (page - 1) * PAGE_SIZE;
        int limit = PAGE_SIZE + NEXT_PAGE_CHECK_SIZE; // 다음 페이지 존재 여부 체크용

        List<InquiryReadAdminResponse> responses = queryFactory.select(
                constructor(InquiryReadAdminResponse.class,
                        inquiry.id.as("inquiryId"),
                        inquiry.author.id.as("authorId"),
                        inquiry.author.nickname.as("authorNickname"),
                        inquiry.title,
                        inquiry.content,
                        inquiry.answer,
                        inquiry.state,
                        inquiry.createdAt
                ))
                .from(inquiry)
                .join(inquiry.author, member)
                .where(stateEq(state))
                .orderBy(inquiry.createdAt.desc())
                .offset(offset)
                .limit(limit) // 다음 페이지 존재 여부 체크용
                .fetch();

        boolean hasNext = responses.size() > PAGE_SIZE;
        List<InquiryReadAdminResponse> limitedPage = getLimitedPage(responses, hasNext);
        Long totalCount = countByState(state);

        return new InquiryListAdminResponse(limitedPage, hasNext, totalCount);
    }

    private BooleanExpression stateEq(InquiryState state) {
        return state == null ? inquiry.state.eq(InquiryState.PENDING) : inquiry.state.eq(state);
    }

    private Long countByState(InquiryState state) {
        return queryFactory
                .select(inquiry.countDistinct())
                .from(inquiry)
                .where(stateEq(state))
                .fetchOne();
    }

    private List<InquiryReadAdminResponse> getLimitedPage(List<InquiryReadAdminResponse> responses, boolean hasNext) {
        if (hasNext) {
            return responses.subList(0, PAGE_SIZE); // 실제로는 limit 만큼만 반환
        }
        return responses;
    }

}