package org.kwakmunsu.randsome.admin.inquiry.serivce;

import org.kwakmunsu.randsome.admin.inquiry.repository.dto.InquiryListAdminResponse;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;

public interface InquiryAdminRepository {

    Inquiry findById(Long id);

    InquiryListAdminResponse findAllByInquiryStatus(InquiryStatus state, int page);

}