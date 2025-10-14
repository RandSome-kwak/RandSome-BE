package org.kwakmunsu.randsome.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class InquiryCommandServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private InquiryRepository inquiryRepository;

    @InjectMocks
    private InquiryCommandService inquiryCommandService;

    @DisplayName("문의를 등록한다.")
    @Test
    void register() {
        // given
        var author = MemberFixture.createMember(1L);
        var request = InquiryRegisterServiceRequest.builder()
                .authorId(author.getId())
                .title("문의 제목")
                .content("문의 내용")
                .build();
        var inquiry = Inquiry.create(author, request.title(), request.content());
        ReflectionTestUtils.setField(inquiry, "id", 1L);

        given(memberRepository.findById(any(Long.class))).willReturn(author);
        given(inquiryRepository.save(any(Inquiry.class))).willReturn(inquiry);

        // when
        var inquiryId = inquiryCommandService.register(request);

        // then
        assertThat(inquiryId).isEqualTo(inquiry.getId());
    }

}