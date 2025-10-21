package org.kwakmunsu.randsome.admin.member.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.PerformanceTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class MemberDataInitializer extends PerformanceTestSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int TOTAL_COUNT = 5_000_000;
    private static final int BATCH_SIZE = 10_000;
    private static final List<String> GENDERS = Arrays.asList("M", "F");
    private static final List<String> MBTIS = Arrays.asList(
            "ENFJ", "ENFP", "ENTJ", "ENTP",
            "ESFJ", "ESFP", "ESTJ", "ESTP",
            "INFJ", "INFP", "INTJ", "INTP",
            "ISFJ", "ISFP", "ISTJ", "ISTP"
    );

    @Test
    public void generateMembers() {
        String sql = """
            INSERT INTO members (
                login_id, password, legal_name, nickname, gender, role, mbti,
                instagram_id, introduction, ideal_description, refresh_token,
                created_at, updated_at, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        Random random = new Random();
        long startTime = System.currentTimeMillis();

        for (int currentIndex = 0; currentIndex < TOTAL_COUNT; currentIndex += BATCH_SIZE) {
            final int startIdx = currentIndex;
            final int endIdx = Math.min(currentIndex + BATCH_SIZE, TOTAL_COUNT);
            final int batchCount = endIdx - startIdx;

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues( PreparedStatement ps, int i) throws SQLException {
                    int idx = startIdx + i;

                    ps.setString(1, "user_" + idx + "@test.com");
                    ps.setString(2, "$2a$10$encodedPasswordHash"); // BCrypt 인코딩된 비밀번호
                    ps.setString(3, "테스트유저_" + idx);
                    ps.setString(4, "nickname_" + idx);
                    ps.setString(5, GENDERS.get(idx % 2));
                    ps.setString(6, "ROLE_MEMBER");
                    ps.setString(7, MBTIS.get(random.nextInt(MBTIS.size())));
                    ps.setString(8, "insta_" + idx);
                    ps.setString(9, "안녕하세요 " + idx + "번 유저입니다.");
                    ps.setString(10, "이상형 설명 " + idx);
                    ps.setString(11, null);

                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    ps.setTimestamp(12, now);
                    ps.setTimestamp(13, now);
                    ps.setString(14, "ACTIVE");
                }

                @Override
                public int getBatchSize() {
                    return batchCount;
                }
            });

            // 진행상황 출력
            if ((currentIndex + BATCH_SIZE) % 100_000 == 0) {
                long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                System.out.printf("Progress: %,d / %,d (경과 시간: %d초)%n",
                        currentIndex + BATCH_SIZE, TOTAL_COUNT, elapsed);
            }
        }

        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.printf("완료! 총 %,d건 생성 (소요 시간: %d초)%n", TOTAL_COUNT, totalTime);

        // 결과 확인
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM members", Integer.class);
        System.out.println("실제 삽입된 데이터: " + count);
    }


}