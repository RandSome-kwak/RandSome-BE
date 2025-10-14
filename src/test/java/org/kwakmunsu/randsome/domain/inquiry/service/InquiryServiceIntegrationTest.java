package org.kwakmunsu.randsome.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryUpdateServiceRequest;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.kwakmunsu.randsome.global.exception.ConflictException;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class InquiryServiceIntegrationTest {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private  MemberRepository memberRepository;

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private EntityManager entityManager;

    private Member author;
    private Inquiry inquiry;

    @BeforeEach
    void setUp() {
        author = MemberFixture.createMember();
        memberRepository.save(author);
        inquiry = Inquiry.create(author, "title", "content");
        inquiryRepository.save(inquiry);
    }

    @DisplayName("답변이 등록되지 않은 자신의 문의 글을 수정한다. ")
    @Test
    void updateInquiry() {
        // given
        var request = getInquiryUpdateServiceRequest(inquiry.getId(), author.getId());

        // when
        inquiryService.updateInquiry(request);
        entityManager.flush();

        // then
        var updated = inquiryRepository.findById(inquiry.getId());

        assertThat(updated).extracting(Inquiry::getTitle, Inquiry::getContent, Inquiry::getStatus)
                .containsExactly(request.title(), request.content(), InquiryStatus.PENDING);
    }

    @DisplayName("답변이 등록되었을 경우 문의 글을 수정할 수 없다.")
    @Test
    void failInquiry() {
        // given
        inquiry.registerAnswer("answer");
        entityManager.flush();
        var request = getInquiryUpdateServiceRequest(inquiry.getId(), author.getId());

        // when & then
        assertThatThrownBy(() -> inquiryService.updateInquiry(request))
            .isInstanceOf(ConflictException.class);
    }

    @DisplayName("자신의 문의 글이 아닐 경우 수정할 수 없다.")
    @Test
    void failUpdateNotFound() {
        // given
        var invalidAuthorId = author.getId() + 1;
        var request = getInquiryUpdateServiceRequest(inquiry.getId(), invalidAuthorId);

        // when & then
        assertThatThrownBy(() -> inquiryService.updateInquiry(request))
            .isInstanceOf(NotFoundException.class);
    }

    private InquiryUpdateServiceRequest getInquiryUpdateServiceRequest(Long inquiryId, Long authorId) {
        return InquiryUpdateServiceRequest.builder()
                .inquiryId(inquiryId)
                .authorId(authorId)
                .title("update title")
                .content("update content")
                .build();
    }

}