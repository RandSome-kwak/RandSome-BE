package org.kwakmunsu.randsome.domain.matching.repository;

import static org.kwakmunsu.randsome.domain.matching.entity.QMatchingApplication.matchingApplication;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingApplicationQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<MatchingApplication> findAllByRequesterIdAndMatchingStatusIn(
            Long requesterId,
            List<MatchingStatus> statuses,
            int limit,
            Long lastApplicationId
    ) {
        return queryFactory.selectFrom(matchingApplication)
                .where(
                        requesterIdEq(requesterId),
                        cursorIdCondition(lastApplicationId),
                        isActive(),
                        matchingStatusIn(statuses)
                )
                .orderBy(matchingApplication.id.desc())
                .limit(limit + 1)
                .fetch();
    }

    private BooleanExpression cursorIdCondition(Long lastApplicationId) {
        if (lastApplicationId == null) {
            return null;
        }

        return matchingApplication.id.lt(lastApplicationId);
    }


    private BooleanExpression requesterIdEq(Long requesterId) {
        if (requesterId == null) {
            return null;
        }
        return matchingApplication.requester.id.eq(requesterId);
    }

    private BooleanExpression isActive() {
        return matchingApplication.status.eq(EntityStatus.ACTIVE);
    }

    private BooleanExpression matchingStatusIn(List<MatchingStatus> statues) {
        if (statues.isEmpty()) {
            return null;
        }
        return matchingApplication.matchingStatus.in(statues);
    }

}