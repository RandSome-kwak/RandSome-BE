package org.kwakmunsu.randsome.admin.matching.repository.matchingapplication;

import static com.querydsl.core.types.Projections.constructor;
import static org.kwakmunsu.randsome.domain.matching.entity.QMatchingApplication.matchingApplication;
import static org.kwakmunsu.randsome.domain.payment.entity.QPayment.payment;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminListResponse;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminPreviewResponse;
import org.kwakmunsu.randsome.domain.payment.enums.PaymentType;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingApplicationQueryDslRepository {

    private static final int PAGE_SIZE = 20;
    private static final int NEXT_PAGE_CHECK_SIZE = 1;
    private final JPAQueryFactory queryFactory;

    public MatchingApplicationAdminListResponse findAllByStatus(MatchingStatus status, int page) {
        int offset = (page - 1) * PAGE_SIZE;
        int limit = PAGE_SIZE + NEXT_PAGE_CHECK_SIZE; // 다음 페이지 존재 여부 체크용

        List<MatchingApplicationAdminPreviewResponse> responses = queryFactory.select(
                        constructor(
                                MatchingApplicationAdminPreviewResponse.class,
                                matchingApplication.id,
                                matchingApplication.requester.id,
                                matchingApplication.requester.legalName,
                                matchingApplication.requester.nickname,
                                matchingApplication.requester.gender,
                                matchingApplication.matchingType,
                                matchingApplication.requestedCount.as("matchingCount"),
                                payment.amount.as("price"),
                                matchingApplication.createdAt.as("appliedAt"),
                                matchingApplication.status.as("status")
                        ))
                .from(matchingApplication)
                .join(payment).on(matchingApplication.requester.id.eq(payment.member.id))
                .where(
                        payment.type.in(PaymentType.IDEAL_MATCHING, PaymentType.RANDOM_MATCHING),
                        statusEq(status)
                )
                .orderBy(matchingApplication.createdAt.desc())
                .offset(offset)
                .limit(limit)
                .fetch();

        boolean hasNext = responses.size() > PAGE_SIZE;
        List<MatchingApplicationAdminPreviewResponse> limitedPage = getLimitedPage(responses, hasNext);
        Long totalCount = countByStatus(status);

        return new MatchingApplicationAdminListResponse(limitedPage, hasNext, totalCount);
    }

    public Long countByStatus(MatchingStatus status) {
        return queryFactory
                .select(matchingApplication.countDistinct())
                .from(matchingApplication)
                .join(payment).on(matchingApplication.requester.id.eq(payment.member.id))
                .where(
                        payment.type.in(PaymentType.IDEAL_MATCHING, PaymentType.RANDOM_MATCHING),
                        statusEq(status)
                )
                .fetchOne();
    }

    private BooleanExpression statusEq(MatchingStatus status) {
        // 기본값 : PENDING
        MatchingStatus matchingStatus = status != null ? status : MatchingStatus.PENDING;
        return matchingApplication.status.eq(matchingStatus);
    }

    private List<MatchingApplicationAdminPreviewResponse> getLimitedPage(List<MatchingApplicationAdminPreviewResponse> responses,
            boolean hasNext) {
        if (hasNext) {
            return responses.subList(0, PAGE_SIZE); // 실제로는 limit 만큼만 반환
        }
        return responses;
    }

}
