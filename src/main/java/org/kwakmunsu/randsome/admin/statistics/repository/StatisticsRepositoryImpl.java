package org.kwakmunsu.randsome.admin.statistics.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.kwakmunsu.randsome.admin.statistics.service.StatisticsRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
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