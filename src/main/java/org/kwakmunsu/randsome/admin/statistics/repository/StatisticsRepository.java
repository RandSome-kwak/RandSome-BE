package org.kwakmunsu.randsome.admin.statistics.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public long findPendingApprovals(String pendingStatus) {
        String sql = """
            SELECT
              (SELECT COUNT(*) FROM candidates WHERE candidate_status = :pendingStatus AND status = 'ACTIVE')
            + (SELECT COUNT(*) FROM matching_applications WHERE matching_status = :pendingStatus AND status = 'ACTIVE')
            + (SELECT COUNT(*) FROM inquiries WHERE inquiry_status = :pendingStatus AND status = 'ACTIVE') 
        """;
        Object result = entityManager.createNativeQuery(sql)
                .setParameter("pendingStatus", pendingStatus)
                .getSingleResult();
        return ((Number) result).longValue();
    }

}