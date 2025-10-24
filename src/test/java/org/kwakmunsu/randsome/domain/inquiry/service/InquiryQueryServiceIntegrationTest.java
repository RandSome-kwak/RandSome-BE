package org.kwakmunsu.randsome.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryListResponse;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record InquiryQueryServiceIntegrationTest(
        InquiryQueryService inquiryQueryService,
        InquiryRepository inquiryRepository,
        MemberRepository memberRepository,
        EntityManager entityManager
) {

    @DisplayName("삭제된 문의 조회 확인 테스트")
    @Test
    void ReadDelete()   {
        // given
        var author = MemberFixture.createMember();
        memberRepository.save(author);

        var inquiry = Inquiry.create(author, "title", "content");
        inquiryRepository.save(inquiry);

        inquiry.delete();
        entityManager.flush();

        // when
        InquiryListResponse inquires = inquiryQueryService.getInquires(author.getId());

        // then
        assertThat(inquires.inquiries()).isEmpty();
    }

}