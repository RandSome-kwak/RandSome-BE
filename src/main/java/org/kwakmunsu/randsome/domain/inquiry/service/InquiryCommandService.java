package org.kwakmunsu.randsome.domain.inquiry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryUpdateServiceRequest;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.global.exception.ConflictException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InquiryCommandService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    public Long register(InquiryRegisterServiceRequest request) {
        Member author = memberRepository.findById(request.authorId());

        Inquiry inquiry = Inquiry.create(author, request.title(), request.content());
        Inquiry saved = inquiryRepository.save(inquiry);

        log.info("[new Inquiry]. authorId = {}, title = {}, ", author.getId(), inquiry.getTitle());

        return saved.getId();
    }

    @Transactional
    public void update(InquiryUpdateServiceRequest request) {
        Inquiry inquiry = inquiryRepository.findByIdAndAuthorId(request.inquiryId(), request.authorId());

        // 답변이 완료된 문의글은 수정 불가
        inquiry.updateQuestion(request.title(), request.content());
    }

    @Transactional
    public void delete(Long inquiryId, Long authorId) {
        Inquiry inquiry = inquiryRepository.findByIdAndAuthorId(inquiryId, authorId);

        if (inquiry.isAnswered()) {
            throw new ConflictException(ErrorStatus.CANNOT_DELETE_ANSWERED_INQUIRY);
        }

        // 논리적 delete
        inquiry.delete();
    }

}