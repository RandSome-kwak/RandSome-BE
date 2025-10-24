package org.kwakmunsu.randsome.domain.inquiry.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryListResponse;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryReadResponse;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InquiryQueryService {

    private final InquiryRepository inquiryRepository;

    public InquiryListResponse getInquires(Long authorId) {
        List<Inquiry> inquiries = inquiryRepository.findAllByAuthorIdAndStatus(authorId, EntityStatus.ACTIVE);

        List<InquiryReadResponse> responses = inquiries.stream()
                .map(InquiryReadResponse::from)
                .toList();

        return new InquiryListResponse(responses);
    }


}