package org.kwakmunsu.randsome.domain.inquiry.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.kwakmunsu.randsome.domain.inquiry.repository.dto.InquiryListAdminResponse;
import org.kwakmunsu.randsome.domain.inquiry.service.InquiryRepository;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryRepositoryImpl implements InquiryRepository {

    private final InquiryJpaRepository inquiryJpaRepository;
    private final InquiryQueryDslRepository inquiryQueryDslRepository;

    @Override
    public Inquiry save(Inquiry inquiry) {
        return inquiryJpaRepository.save(inquiry);
    }

    @Override
    public List<Inquiry> findAllByAuthorId(Long authorId) {
        return inquiryJpaRepository.findAllByAuthorId(authorId);
    }

    @Override
    public Inquiry findById(Long id) {
        return inquiryJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_INQUIRY));
    }

    @Override
    public InquiryListAdminResponse findAllByState(InquiryStatus state, int page) {
        return inquiryQueryDslRepository.findAllByState(state, page);
    }

    @Override
    public Inquiry findByIdAndAuthorId(Long id, Long authorId) {
        return inquiryJpaRepository.findByIdAndAuthorId(id, authorId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_INQUIRY));
    }

}