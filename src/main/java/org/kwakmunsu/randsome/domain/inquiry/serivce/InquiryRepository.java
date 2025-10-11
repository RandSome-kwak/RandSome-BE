package org.kwakmunsu.randsome.domain.inquiry.serivce;

import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;

public interface InquiryRepository {

    Inquiry save(Inquiry inquiry);

}