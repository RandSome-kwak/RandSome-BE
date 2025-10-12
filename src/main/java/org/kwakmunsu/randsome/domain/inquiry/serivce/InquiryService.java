package org.kwakmunsu.randsome.domain.inquiry.serivce;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.serivce.dto.InquiryListResponse;
import org.kwakmunsu.randsome.domain.inquiry.serivce.dto.InquiryReadResponse;
import org.kwakmunsu.randsome.domain.inquiry.serivce.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    public Long registerInquiry(InquiryRegisterServiceRequest request) {
        Member author = memberRepository.findById(request.authorId());

        Inquiry inquiry = Inquiry.create(author, request.title(), request.content());
        Inquiry saved = inquiryRepository.save(inquiry);

        log.info("[new Inquiry]. authorId = {}, title = {}, ", author.getId(), inquiry.getTitle());

        return saved.getId();
    }

    public InquiryListResponse getInquires(Long authorId) {
        List<Inquiry> inquiries = inquiryRepository.findAllByAuthorId(authorId);

        List<InquiryReadResponse> responses = inquiries.stream()
                .map(InquiryReadResponse::from)
                .toList();

        return new InquiryListResponse(responses);
    }

}