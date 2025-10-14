package org.kwakmunsu.randsome.admin.inquiry.serivce;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.kwakmunsu.randsome.domain.inquiry.repository.dto.InquiryListAdminResponse;
import org.kwakmunsu.randsome.domain.inquiry.serivce.InquiryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InquiryAdminService {

    private final InquiryRepository inquiryRepository;

    @Transactional
    public void registerAnswer(Long inquiryId, String answer) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId);

        // 답변이 이미 등록된 경우 예외를 던진다.
        inquiry.registerAnswer(answer);
    }

    public InquiryListAdminResponse getInquires(InquiryStatus state, int page) {
        return inquiryRepository.findAllByState(state, page);
    }

}