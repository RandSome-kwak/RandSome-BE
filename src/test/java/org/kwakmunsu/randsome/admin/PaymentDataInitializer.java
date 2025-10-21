package org.kwakmunsu.randsome.admin;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.PerformanceTestSupport;
import org.kwakmunsu.randsome.domain.payment.enums.PaymentStatus;
import org.kwakmunsu.randsome.domain.payment.enums.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class PaymentDataInitializer extends PerformanceTestSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int TOTAL_COUNT = 5_000_000;
    private static final int BATCH_SIZE = 10_000;

    @Test
    public void generatePayments() {
        Integer memberCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM members", Integer.class);
        if (memberCount == null || memberCount == 0) {
            throw new IllegalStateException("Members 데이터가 없습니다.");
        }

        List<PaymentType> paymentTypes = List.of(PaymentType.values());
        List<PaymentStatus> paymentStatuses = List.of(PaymentStatus.values());

        String sql = """
            INSERT INTO payments (
                member_id, amount, payment_status, type,
                created_at, updated_at, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        Random random = new Random();

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE payments");
        jdbcTemplate.execute("SET autocommit = 0");

        try {
            for (int offset = 0; offset < TOTAL_COUNT; offset += BATCH_SIZE) {
                final int batchStart = offset;
                int batchEnd = Math.min(offset + BATCH_SIZE, TOTAL_COUNT);

                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        long memberId = random.nextInt(memberCount) + 1L;
                        PaymentType type = paymentTypes.get(random.nextInt(paymentTypes.size()));
                        int matchingCount = 1 + random.nextInt(5);
                        BigDecimal amount = BigDecimal.valueOf(type.getAmount() * matchingCount);
                        PaymentStatus status = paymentStatuses.get(random.nextInt(paymentStatuses.size()));

                        Timestamp now = new Timestamp(System.currentTimeMillis());

                        ps.setLong(1, memberId);
                        ps.setBigDecimal(2, amount);
                        ps.setString(3, status.name());
                        ps.setString(4, type.name());
                        ps.setTimestamp(5, now);
                        ps.setTimestamp(6, now);
                        ps.setString(7, "ACTIVE");
                    }

                    @Override
                    public int getBatchSize() {
                        return batchEnd - batchStart;
                    }
                });

                if ((batchEnd / BATCH_SIZE) % 10 == 0) { // 10번째 batch 마다 커밋 및 로그
                    jdbcTemplate.execute("COMMIT");
                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    System.out.printf("Processed %,d records (%d seconds elapsed)%n", batchEnd, elapsed);
                }
            }
            jdbcTemplate.execute("COMMIT");
        } finally {
            jdbcTemplate.execute("SET autocommit = 1");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    private long startTime = System.currentTimeMillis();
}
