package org.kwakmunsu.randsome.admin.inquiry.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryAdminRepository {

    private final InquiryAdminJpaRepository inquiryAdminJpaRepository;
    private final InquiryAdminQueryDslRepository inquiryAdminQueryDslRepository;

    public Inquiry findById(Long id) {
        return inquiryAdminJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_INQUIRY));
    }

    public List<Inquiry> findAllByInquiryStatusAndStatus(InquiryStatus inquiryStatus, int offset, int limit, EntityStatus status) {
        return inquiryAdminQueryDslRepository.findAllByInquiryStatusAndStatus(inquiryStatus, offset, limit, status);
    }

    public long countByInquiryStatusAndStatus(InquiryStatus inquiryStatus, EntityStatus status) {
        return inquiryAdminJpaRepository.countByInquiryStatusAndStatus(inquiryStatus, status);
    }

}
