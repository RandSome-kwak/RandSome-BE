package org.kwakmunsu.randsome.admin.inquiry.serivce;

import java.util.List;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;

public interface InquiryAdminRepository {

    Inquiry findById(Long id);

    List<Inquiry> findAllByInquiryStatusAndStatus(InquiryStatus state, int offset, int limit, EntityStatus status);

    long countByInquiryStatusAndStatus(InquiryStatus inquiryStatus, EntityStatus status);
}