package org.kwakmunsu.randsome.domain.inquiry.serivce;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.serivce.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private InquiryRepository inquiryRepository;

    @InjectMocks
    private InquiryService inquiryService;

    @DisplayName("문의를 등록한다.")
    @Test
    void registerInquiry() {
        // given
        var author = MemberFixture.createMember(1L);
        var request = InquiryRegisterServiceRequest.builder()
                .authorId(author.getId())
                .title("문의 제목")
                .content("문의 내용")
                .build();
        var inquiry = Inquiry.create(author, request.title(), request.content());
        ReflectionTestUtils.setField(inquiry, "id", 1L);

        given (memberRepository.findById(any(Long.class))).willReturn(author);
        given(inquiryRepository.save(any(Inquiry.class))).willReturn(inquiry);

        // when
        var inquiryId = inquiryService.registerInquiry(request);

        // then
        assertThat(inquiryId).isEqualTo(inquiry.getId());
    }

}