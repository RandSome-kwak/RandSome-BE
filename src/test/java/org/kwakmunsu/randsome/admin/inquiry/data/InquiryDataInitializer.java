package org.kwakmunsu.randsome.admin.inquiry.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.PerformanceTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@Disabled
public class InquiryDataInitializer extends PerformanceTestSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int TOTAL_COUNT = 5_000_000;
    private static final int BATCH_SIZE = 10_000;

    private static final List<String> TITLES = Arrays.asList(
            "회원 탈퇴 문의",
            "매칭 관련 문의",
            "결제 오류 문의",
            "프로필 수정 문의",
            "신고 접수",
            "서비스 이용 문의",
            "버그 리포트",
            "계정 복구 요청",
            "환불 문의",
            "기능 개선 제안"
    );

    @Test
    public void generateInquiriesSingleThread() {
        // Member 수 확인
        Integer memberCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM members", Integer.class
        );
        System.out.println("Total Members: " + memberCount);

        if (memberCount < 1000) {
            throw new IllegalStateException("Member 데이터가 충분하지 않습니다. 최소 1000건 필요");
        }

        String sql = """
            INSERT INTO inquiries (
                author_id, title, content, inquiry_status, answer,
                created_at, updated_at, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
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

                        long authorId = (long) (random.nextInt(memberCount) + 1);
                        boolean isCompleted = random.nextInt(100) < 20;
                        String statusValue = isCompleted ? "COMPLETED" : "PENDING";
                        String title = TITLES.get(random.nextInt(TITLES.size())) + " #" + idx;
                        String content = "문의 내용입니다. " + idx + "번 문의입니다. 상세 내용: " + generateRandomContent(random);
                        String answer = isCompleted ? "답변드립니다. " + idx + "번 문의에 대한 답변입니다." : null;

                        ps.setLong(1, authorId);
                        ps.setString(2, title);
                        ps.setString(3, content);
                        ps.setString(4, statusValue);
                        ps.setString(5, answer);

                        long randomTime = System.currentTimeMillis() - (long)(random.nextDouble() * 30L * 24 * 60 * 60 * 1000);
                        Timestamp createdAt = new Timestamp(randomTime);
                        ps.setTimestamp(6, createdAt);
                        ps.setTimestamp(7, createdAt);
                        ps.setString(8, "ACTIVE");
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
                    System.out.printf("Progress: %,d / %,d (%.1f%%), 경과 시간: %d초, 속도: %.0f건/초%n",
                            currentIndex + BATCH_SIZE, TOTAL_COUNT,
                            ((currentIndex + BATCH_SIZE) * 100.0 / TOTAL_COUNT), elapsed, rate);
                }
            }
            jdbcTemplate.execute("COMMIT");
        } finally {
            jdbcTemplate.execute("SET autocommit = 1");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        }

        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.printf("완료! 총 %,d건 생성 (총 소요 시간: %d초)%n", TOTAL_COUNT, totalTime);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM inquiries", Integer.class);
        System.out.println("실제 삽입된 데이터: " + count);
    }

    private String generateRandomContent(Random random) {
        String[] contentTemplates = {
                "서비스 이용 중 문제가 발생했습니다.",
                "매칭 기능에 대해 궁금한 점이 있습니다.",
                "결제 진행이 원활하지 않습니다.",
                "프로필 사진 업로드가 되지 않습니다.",
                "앱 실행 시 오류가 발생합니다.",
                "회원 탈퇴 절차를 알고 싶습니다.",
                "신고한 사용자에 대한 조치가 궁금합니다.",
                "포인트 충전 후 반영이 안됩니다."
        };
        return contentTemplates[random.nextInt(contentTemplates.length)];
    }
}
