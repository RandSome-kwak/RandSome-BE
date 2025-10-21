package org.kwakmunsu.randsome.admin.matching.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.PerformanceTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class MatchingDataInitializer extends PerformanceTestSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int PAGE_SIZE = 10_000;

    @Test
    public void generateMatchings() {
        Integer completedAppCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM matching_applications WHERE matching_status = 'COMPLETED'",
                Integer.class
        );
        System.out.println("Total Completed Applications: " + completedAppCount);

        Integer memberCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM members", Integer.class
        );
        System.out.println("Total Members: " + memberCount);

        String sql = """
            INSERT INTO matchings (
                application_id, selected_member_id, created_at, updated_at, status
            ) VALUES (?, ?, ?, ?, ?)
        """;

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("SET autocommit = 0");
        jdbcTemplate.execute("TRUNCATE TABLE matchings");

        long totalMatchings = 0;
        long startTime = System.currentTimeMillis();

        try {
            Random random = new Random();

            for (int offset = 0; offset < completedAppCount; offset += PAGE_SIZE) {
                List<ApplicationInfo> apps = jdbcTemplate.query(
                        """
                        SELECT id, requested_count, created_at 
                        FROM matching_applications 
                        WHERE matching_status = 'COMPLETED'
                        ORDER BY id
                        LIMIT ? OFFSET ?
                        """,
                        new Object[]{PAGE_SIZE, offset},
                        (rs, rowNum) -> new ApplicationInfo(
                                rs.getLong("id"),
                                rs.getInt("requested_count"),
                                rs.getTimestamp("created_at")
                        )
                );

                // 매칭 데이터 전체 모으기
                List<MatchingRecord> batchData = new ArrayList<>();
                for (ApplicationInfo app : apps) {
                    for (int i = 0; i < app.requestedCount; i++) {
                        long selectedMemberId = random.nextInt(memberCount) + 1L;
                        batchData.add(new MatchingRecord(
                                app.applicationId,
                                selectedMemberId,
                                app.createdAt,
                                app.createdAt,
                                "ACTIVE"
                        ));
                    }
                }

                // 배치 삽입
                final List<MatchingRecord> batch = batchData;
                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MatchingRecord record = batch.get(i);
                        ps.setLong(1, record.applicationId);
                        ps.setLong(2, record.selectedMemberId);
                        ps.setTimestamp(3, record.createdAt);
                        ps.setTimestamp(4, record.updatedAt);
                        ps.setString(5, record.status);
                    }

                    @Override
                    public int getBatchSize() {
                        return batch.size();
                    }
                });

                totalMatchings += batchData.size();

                if (offset > 0 && offset % 100_000 == 0) {
                    jdbcTemplate.execute("COMMIT");
                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    double speed = totalMatchings / (elapsed > 0 ? elapsed : 1.0);
                    System.out.printf("Progress: Applications %,d / %,d, Matchings: %,d (%.0f건/초)%n",
                            offset, completedAppCount, totalMatchings, speed);
                }
            }
            jdbcTemplate.execute("COMMIT");
        } finally {
            jdbcTemplate.execute("SET autocommit = 1");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        }

        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.printf("완료! 총 %,d건 Matching 생성 (소요 시간: %d초)%n", totalMatchings, totalTime);
    }

    private static class ApplicationInfo {
        long applicationId;
        int requestedCount;
        Timestamp createdAt;

        ApplicationInfo(long applicationId, int requestedCount, Timestamp createdAt) {
            this.applicationId = applicationId;
            this.requestedCount = requestedCount;
            this.createdAt = createdAt;
        }
    }

    private static class MatchingRecord {
        long applicationId;
        long selectedMemberId;
        Timestamp createdAt;
        Timestamp updatedAt;
        String status;

        MatchingRecord(long applicationId, long selectedMemberId,
                Timestamp createdAt, Timestamp updatedAt,
                String status) {
            this.applicationId = applicationId;
            this.selectedMemberId = selectedMemberId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.status = status;
        }
    }
}
