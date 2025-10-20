package org.kwakmunsu.randsome.admin.matching.repository.matchingapplication;

import static com.querydsl.core.types.Projections.constructor;
import static org.kwakmunsu.randsome.domain.matching.entity.QMatchingApplication.matchingApplication;
import static org.kwakmunsu.randsome.domain.payment.entity.QPayment.payment;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminPreviewResponse;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.payment.enums.PaymentType;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingApplicationQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<MatchingApplicationAdminPreviewResponse> findAllByMatchingStatusAndStatus(MatchingStatus matchingStatus, int offset, int limit,
            EntityStatus status) {

        return queryFactory.select(
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
                                matchingApplication.matchingStatus.as("matchingStatus")
                        ))
                .from(matchingApplication)
                .join(payment).on(matchingApplication.requester.id.eq(payment.member.id))
                .where(
                        payment.type.in(PaymentType.IDEAL_MATCHING, PaymentType.RANDOM_MATCHING),
                        matchingStatusEq(matchingStatus),
                        statusEq(status)
                )
                .orderBy(matchingApplication.id.desc())
                .offset(offset)
                .limit(limit)
                .fetch();

    }

    private BooleanExpression statusEq(EntityStatus status) {
        return matchingApplication.status.eq(status);
    }


    private BooleanExpression matchingStatusEq(MatchingStatus status) {
        // 기본값 : PENDING
        MatchingStatus matchingStatus = status != null ? status : MatchingStatus.PENDING;
        return matchingApplication.matchingStatus.eq(matchingStatus);
    }

}