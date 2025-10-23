package org.kwakmunsu.randsome.admin.matching.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.PerformanceTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@Disabled
public class MatchingApplicationDataInitializer extends PerformanceTestSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int TOTAL_COUNT = 5_000_000;
    private static final int BATCH_SIZE = 10_000;

    @Test
    public void generateMatchingApplications() {
        // Member 수 확인
        Integer memberCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM members", Integer.class
        );
        System.out.println("Total Members: " + memberCount);

        String sql = """
            INSERT INTO matching_applications (
                requester_id, requested_count, matching_status, matching_type,
                created_at, updated_at, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        Random random = new Random();
        long startTime = System.currentTimeMillis();

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("SET autocommit = 0");

        try {
            for (int currentIndex = 0; currentIndex < TOTAL_COUNT; currentIndex += BATCH_SIZE) {
                final int startIdx = currentIndex;
                final int endIdx = Math.min(currentIndex + BATCH_SIZE, TOTAL_COUNT);
                final int batchCount = endIdx - startIdx;

                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        int idx = startIdx + i;

                        // 랜덤 요청자 (1 ~ memberCount)
                        long requesterId = (long) (random.nextInt(memberCount) + 1);

                        // 요청 인원: 1명(10%), 2명(30%), 3명(40%), 4명(20%)
                        int requestedCount = getWeightedRequestCount(random);

                        // 매칭 상태: COMPLETED(70%), PENDING(20%), FAILED(10%)
                        String status = getWeightedStatus(random);

                        // 매칭 타입: RANDOM(80%), IDEAL_TYPE(20%)
                        String type = random.nextInt(100) < 80 ? "RANDOM" : "IDEAL_TYPE";

                        ps.setLong(1, requesterId);
                        ps.setInt(2, requestedCount);
                        ps.setString(3, status);
                        ps.setString(4, type);

                        // 최근 90일 이내 랜덤 날짜
                        long randomTime = System.currentTimeMillis()
                                - (long) (random.nextDouble() * 90L * 24 * 60 * 60 * 1000);
                        Timestamp createdAt = new Timestamp(randomTime);
                        ps.setTimestamp(5, createdAt);

                        // COMPLETED는 created_at + 몇 시간 후
                        Timestamp updatedAt = status.equals("COMPLETED")
                                ? new Timestamp(createdAt.getTime() + (long)(random.nextDouble() * 24 * 60 * 60 * 1000))
                                : createdAt;
                        ps.setTimestamp(6, updatedAt);
                        ps.setString(7, "ACTIVE");
                    }

                    @Override
                    public int getBatchSize() {
                        return batchCount;
                    }
                });

                if ((currentIndex + BATCH_SIZE) % 100_000 == 0) {
                    jdbcTemplate.execute("COMMIT");

                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    double rate = (currentIndex + BATCH_SIZE) / (elapsed > 0 ? elapsed : 1.0);
                    System.out.printf("Progress: %,d / %,d (%.1f%%, 경과: %d초, 속도: %.0f건/초)%n",
                            currentIndex + BATCH_SIZE, TOTAL_COUNT,
                            ((currentIndex + BATCH_SIZE) * 100.0 / TOTAL_COUNT),
                            elapsed, rate);
                }
            }

            jdbcTemplate.execute("COMMIT");

        } finally {
            jdbcTemplate.execute("SET autocommit = 1");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        }

        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.printf("완료! 총 %,d건 생성 (소요 시간: %d초)%n", TOTAL_COUNT, totalTime);

        printStatistics();
    }

    private int getWeightedRequestCount(Random random) {
        int value = random.nextInt(100);
        if (value < 10) return 1;      // 10%
        if (value < 40) return 2;      // 30%
        if (value < 80) return 3;      // 40%
        return 4;                       // 20%
    }

    private String getWeightedStatus(Random random) {
        int value = random.nextInt(100);
        if (value < 70) return "COMPLETED";  // 70%
        if (value < 90) return "PENDING";    // 20%
        return "FAILED";                      // 10%
    }

    private void printStatistics() {
        System.out.println("\n=== MatchingApplication 통계 ===");

        // 상태별 통계
        jdbcTemplate.query(
                "SELECT matching_status, COUNT(*) as cnt FROM matching_applications GROUP BY matching_status",
                (rs, rowNum) -> {
                    System.out.printf("%s: %,d건%n",
                            rs.getString("matching_status"),
                            rs.getInt("cnt"));
                    return null;
                }
        );

        // 타입별 통계
        System.out.println("\n--- 매칭 타입 ---");
        jdbcTemplate.query(
                "SELECT matching_type, COUNT(*) as cnt FROM matching_applications GROUP BY matching_type",
                (rs, rowNum) -> {
                    System.out.printf("%s: %,d건%n",
                            rs.getString("matching_type"),
                            rs.getInt("cnt"));
                    return null;
                }
        );

        // 요청 인원별 통계
        System.out.println("\n--- 요청 인원 ---");
        jdbcTemplate.query(
                "SELECT requested_count, COUNT(*) as cnt FROM matching_applications GROUP BY requested_count ORDER BY requested_count",
                (rs, rowNum) -> {
                    System.out.printf("%d명: %,d건%n",
                            rs.getInt("requested_count"),
                            rs.getInt("cnt"));
                    return null;
                }
        );
    }
}
