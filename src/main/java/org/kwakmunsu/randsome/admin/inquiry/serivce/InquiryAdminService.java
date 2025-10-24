package org.kwakmunsu.randsome.admin.inquiry.serivce;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.inquiry.repository.InquiryAdminRepository;
import org.kwakmunsu.randsome.admin.inquiry.repository.dto.InquiryReadAdminResponse;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InquiryAdminService {

    private final InquiryAdminRepository inquiryAdminRepository;

    @Transactional
    public void registerAnswer(Long inquiryId, String answer) {
        Inquiry inquiry = inquiryAdminRepository.findById(inquiryId);

        // 답변이 이미 등록된 경우 예외를 던진다.
        inquiry.registerAnswer(answer);
    }

    public PageResponse<InquiryReadAdminResponse> getInquires(InquiryStatus status, PageRequest request) {
        List<Inquiry> inquiries = inquiryAdminRepository.findAllByInquiryStatusAndStatus(status, request.offset(), request.limit(), EntityStatus.ACTIVE);
        long count = inquiryAdminRepository.countByInquiryStatusAndStatus(status, EntityStatus.ACTIVE);

        return new PageResponse<>(inquiries.stream()
                .map(InquiryReadAdminResponse::from)
                .toList(),
                count
        );
    }

}