package org.kwakmunsu.randsome.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InquiryQueryServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private InquiryRepository inquiryRepository;

    @InjectMocks
    private InquiryQueryService inquiryQueryService;


    @DisplayName("자신의 문의 내용을 조회한다.")
    @Test
    void getInquires() {
        // given
        var author = MemberFixture.createMember(1L);
        var inquiryCount = 10;
        var inquiries = createInquiries(author, inquiryCount);

        given(inquiryRepository.findAllByAuthorIdAndStatus(any(Long.class), any(EntityStatus.class))).willReturn(inquiries);

        // when
        var inquiryListResponse = inquiryQueryService.getInquires(author.getId());

        // then
        var inquiryReadResponses = inquiryListResponse.inquiries();
        assertThat(inquiryReadResponses).hasSize(inquiryCount);
    }

    @DisplayName("문의 내역이 없을 시 빈 리스트를 반환한다.")
    @Test
    void getEmpty() {
        // given
        given(inquiryRepository.findAllByAuthorIdAndStatus(any(Long.class), any(EntityStatus.class))).willReturn(List.of());

        // when
        var inquiryListResponse = inquiryQueryService.getInquires(1L);

        // then
        var inquiryReadResponses = inquiryListResponse.inquiries();
        assertThat(inquiryReadResponses).isEmpty();
    }

    private List<Inquiry> createInquiries(Member author, int count) {
        List<Inquiry> inquiries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Inquiry inquiry = Inquiry.create(author, "문의 제목 " + (i + 1), "문의 내용 " + (i + 1));
            inquiries.add(inquiry);
        }
        return inquiries;
    }

}