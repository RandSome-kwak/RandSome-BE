package org.kwakmunsu.randsome.admin.candidate.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.PerformanceTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class CandidateDataInitializer extends PerformanceTestSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int TOTAL_COUNT = 5_000_000;
    private static final int BATCH_SIZE = 10_000;

    @Test
    public void generateCandidates() {
        // Member 수 확인
        Integer memberCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM members", Integer.class
        );
        System.out.println("Total Members: " + memberCount);

        if (memberCount < TOTAL_COUNT) {
            throw new IllegalStateException(
                    "Member 데이터가 부족합니다. 필요: " + TOTAL_COUNT + ", 현재: " + memberCount
            );
        }

        String sql = """
            INSERT INTO candidates (
                member_id, candidate_status, created_at, updated_at, status
            ) VALUES (?, ?, ?, ?, ?)
        """;

        Random random = new Random();
        long startTime = System.currentTimeMillis();

        // FK 체크 비활성화
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        // 자동 커밋 비활성화 (성능 향상)
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
                        long memberId = idx + 1;

                        // PENDING: 35%, APPROVED: 60%, REJECTED: 5%
                        String status = getWeightedRandomStatus(random);

                        ps.setLong(1, memberId);
                        ps.setString(2, status);

                        // 최근 60일 이내 랜덤 날짜
                        long randomTime = System.currentTimeMillis()
                                - (long) (random.nextDouble() * 60L * 24 * 60 * 60 * 1000);
                        Timestamp createdAt = new Timestamp(randomTime);
                        ps.setTimestamp(3, createdAt);

                        // APPROVED/REJECTED는 updated_at이 나중
                        Timestamp updatedAt = status.equals("PENDING")
                                ? createdAt
                                : new Timestamp(createdAt.getTime() +
                                        (long)(random.nextDouble() * 7 * 24 * 60 * 60 * 1000));
                        ps.setTimestamp(4, updatedAt);

                        ps.setString(5, "ACTIVE");
                    }

                    @Override
                    public int getBatchSize() {
                        return batchCount;
                    }
                });

                // 10만건마다 커밋 (메모리 관리)
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

            // 최종 커밋
            jdbcTemplate.execute("COMMIT");

        } finally {
            // 설정 복원
            jdbcTemplate.execute("SET autocommit = 1");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        }

        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.printf("완료! 총 %,d건 생성 (소요 시간: %d초)%n", TOTAL_COUNT, totalTime);

        // 결과 확인
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM candidates", Integer.class);
        System.out.println("실제 삽입된 데이터: " + count);

        printStatistics();
    }

    private String getWeightedRandomStatus(Random random) {
        int randomValue = random.nextInt(100);

        if (randomValue < 35) {
            return "PENDING";
        } else if (randomValue < 95) { // 35 + 60
            return "APPROVED";
        } else {
            return "REJECTED";
        }
    }

    private void printStatistics() {
        System.out.println("\n=== Candidate 통계 ===");

        List<StatusCount> stats = jdbcTemplate.query(
                "SELECT candidate_status, COUNT(*) as cnt FROM candidates GROUP BY candidate_status",
                (rs, rowNum) -> new StatusCount(
                        rs.getString("candidate_status"),
                        rs.getInt("cnt")
                )
        );

        stats.forEach(stat ->
                System.out.printf("%s: %,d건 (%.1f%%)%n",
                        stat.status, stat.count, (stat.count * 100.0 / TOTAL_COUNT))
        );
    }

    private static class StatusCount {
        String status;
        int count;

        StatusCount(String status, int count) {
            this.status = status;
            this.count = count;
        }
    }
}
