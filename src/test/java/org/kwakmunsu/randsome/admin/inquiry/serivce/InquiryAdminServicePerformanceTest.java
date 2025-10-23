package org.kwakmunsu.randsome.admin.inquiry.serivce;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.PerformanceTestSupport;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.inquiry.repository.dto.InquiryReadAdminResponse;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.springframework.beans.factory.annotation.Autowired;

class InquiryAdminServicePerformanceTest extends PerformanceTestSupport {

    @Autowired
    private InquiryAdminRepository inquiryAdminRepository;

    @DisplayName("관리자용 문의 목록 조회 성능 테스트")
    @Test
    void test() {
        long startTime = System.currentTimeMillis();

        PageResponse<InquiryReadAdminResponse> response = getInquires(InquiryStatus.PENDING, new PageRequest(400000));
        System.out.println(response.content().size());
        long duration = System.currentTimeMillis() - startTime;

        System.out.printf("getInquires 실행 시간: %d ms%n", duration);
    }

    public PageResponse<InquiryReadAdminResponse> getInquires(InquiryStatus status, PageRequest request) {
        long startFetch = System.currentTimeMillis();
        List<Inquiry> inquiries = inquiryAdminRepository.findAllByInquiryStatusAndStatus(
                status, request.offset(), request.limit(), EntityStatus.ACTIVE);
        long fetchDuration = System.currentTimeMillis() - startFetch;
        System.out.printf("findAllByInquiryStatusAndStatus 쿼리 실행 시간: %d ms%n", fetchDuration);

        long startCount = System.currentTimeMillis();
        long count = inquiryAdminRepository.countByInquiryStatusAndStatus(status, EntityStatus.ACTIVE);
        long countDuration = System.currentTimeMillis() - startCount;
        System.out.printf("countByInquiryStatusAndStatus 쿼리 실행 시간: %d ms%n", countDuration);

        return new PageResponse<>(inquiries.stream()
                .map(InquiryReadAdminResponse::from)
                .toList(),
                count
        );
    }

}