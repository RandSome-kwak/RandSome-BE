package org.kwakmunsu.randsome.domain.inquiry.service;

import java.util.List;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;

public interface InquiryRepository {

    Inquiry save(Inquiry inquiry);

    List<Inquiry> findAllByAuthorIdAndStatus(Long authorId, EntityStatus status);

    Inquiry findByIdAndAuthorIdAndStatus(Long id, Long authorId, EntityStatus status);

}