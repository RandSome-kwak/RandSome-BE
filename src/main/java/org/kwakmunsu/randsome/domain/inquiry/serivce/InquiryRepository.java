package org.kwakmunsu.randsome.domain.inquiry.serivce;

import java.util.List;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;

public interface InquiryRepository {

    Inquiry save(Inquiry inquiry);

    List<Inquiry> findAllByAuthorId(Long authorId);
}