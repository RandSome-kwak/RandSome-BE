package org.kwakmunsu.randsome.admin.inquiry.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.inquiry.repository.dto.InquiryListAdminResponse;
import org.kwakmunsu.randsome.admin.inquiry.serivce.InquiryAdminRepository;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryAdminRepositoryImpl implements InquiryAdminRepository {

    private final InquiryAdminJpaRepository inquiryAdminJpaRepository;
    private final InquiryAdminQueryDslRepository inquiryAdminQueryDslRepository;

    @Override
    public Inquiry findById(Long id) {
        return inquiryAdminJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_INQUIRY));
    }

    @Override
    public InquiryListAdminResponse findAllByInquiryStatus(InquiryStatus state, int page) {
        return inquiryAdminQueryDslRepository.findAllByInquiryStatus(state, page);
    }

}
