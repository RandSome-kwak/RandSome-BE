package org.kwakmunsu.randsome.domain.inquiry.service;

import java.util.List;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;

public interface InquiryRepository {

    Inquiry save(Inquiry inquiry);

    List<Inquiry> findAllByAuthorId(Long authorId);

    Inquiry findByIdAndAuthorId(Long id, Long authorId);

}