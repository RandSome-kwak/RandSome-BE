package org.kwakmunsu.randsome.domain.inquiry.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.serivce.InquiryRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryRepositoryImpl implements InquiryRepository {

    private final InquiryJpaRepository inquiryJpaRepository;

    @Override
    public Inquiry save(Inquiry inquiry) {
        return inquiryJpaRepository.save(inquiry);
    }

}