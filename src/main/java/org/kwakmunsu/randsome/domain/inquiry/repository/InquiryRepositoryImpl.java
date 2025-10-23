package org.kwakmunsu.randsome.domain.inquiry.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.service.InquiryRepository;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryRepositoryImpl implements InquiryRepository {

    private final InquiryJpaRepository inquiryJpaRepository;

    @Override
    public Inquiry save(Inquiry inquiry) {
        return inquiryJpaRepository.save(inquiry);
    }

    @Override
    public List<Inquiry> findAllByAuthorIdAndStatus(Long authorId, EntityStatus status) {
        return inquiryJpaRepository.findAllByAuthorIdAndStatus(authorId, status);
    }

    @Override
    public Inquiry findByIdAndAuthorIdAndStatus(Long id, Long authorId, EntityStatus status) {
        return inquiryJpaRepository.findByIdAndAuthorIdAndStatus(id, authorId, status)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_INQUIRY));
    }

    @Override
    public long countByAuthorIdAndStatus(Long authorId, EntityStatus status) {
        return inquiryJpaRepository.countByAuthorIdAndStatus(authorId, status);
    }

}