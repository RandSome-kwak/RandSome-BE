package org.kwakmunsu.randsome.domain.inquiry.serivce;

import java.util.List;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryState;
import org.kwakmunsu.randsome.domain.inquiry.repository.dto.InquiryListAdminResponse;

public interface InquiryRepository {

    Inquiry save(Inquiry inquiry);

    List<Inquiry> findAllByAuthorId(Long authorId);

    Inquiry findById(Long id);

    InquiryListAdminResponse findAllByState(InquiryState state, int page);
}