package org.kwakmunsu.randsome.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.kwakmunsu.randsome.domain.inquiry.repository.InquiryJpaRepository;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryUpdateServiceRequest;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.global.exception.ConflictException;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record InquiryCommandServiceIntegrationTest(
        InquiryJpaRepository inquiryRepository,
        MemberRepository memberRepository,
        InquiryCommandService inquiryCommandService,
        EntityManager entityManager
) {

    @DisplayName("답변이 등록되지 않은 자신의 문의 글을 수정한다. ")
    @Test
    void update() {
        // given
        var author = getAuthor();
        var inquiry = getInquiry(author);
        var request = getInquiryUpdateServiceRequest(inquiry.getId(), author.getId());

        // when
        inquiryCommandService.update(request);
        entityManager.flush();

        // then
        var updated = inquiryRepository.findById(inquiry.getId()).get();

        assertThat(updated).extracting(Inquiry::getTitle, Inquiry::getContent, Inquiry::getStatus)
                .containsExactly(request.title(), request.content(), InquiryStatus.PENDING);
    }


    @DisplayName("답변이 등록되었을 경우 문의 글을 수정할 수 없다.")
    @Test
    void failInquiry() {
        // given
        var author = getAuthor();
        var inquiry = getInquiry(author);

        inquiry.registerAnswer("answer");
        entityManager.flush();
        var request = getInquiryUpdateServiceRequest(inquiry.getId(), author.getId());

        // when & then
        assertThatThrownBy(() -> inquiryCommandService.update(request))
            .isInstanceOf(ConflictException.class);
    }

    @DisplayName("자신의 문의 글이 아닐 경우 수정할 수 없다.")
    @Test
    void failUpdateNotFound() {
        // given
        var author = getAuthor();
        var inquiry = getInquiry(author);
        var invalidAuthorId = author.getId() + 1;
        var request = getInquiryUpdateServiceRequest(inquiry.getId(), invalidAuthorId);

        // when & then
        assertThatThrownBy(() -> inquiryCommandService.update(request))
            .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("답변이 등록되지 않은 자신의 문의글을 삭제할 수 있다.")
    @Test
    void delete() {
        // given
        var author = getAuthor();
        var inquiry = getInquiry(author);

        // when
        inquiryCommandService.delete(inquiry.getId(), author.getId());
        entityManager.flush();

        // then
        var deleted = inquiryRepository.findById(inquiry.getId()).get();
        assertThat(deleted.isDeleted()).isTrue();
    }

    @DisplayName("답변이 등록되었을 경우 문의글을 삭제할 수 없다.")
    @Test
    void failDelete() {
        // given
        var author = getAuthor();
        var inquiry = getInquiry(author);

        inquiry.registerAnswer("answer");
        entityManager.flush();

        // when & then
        assertThatThrownBy(() -> inquiryCommandService.delete(inquiry.getId(), author.getId()))
            .isInstanceOf(ConflictException.class);
    }

    private InquiryUpdateServiceRequest getInquiryUpdateServiceRequest(Long inquiryId, Long authorId) {
        return InquiryUpdateServiceRequest.builder()
                .inquiryId(inquiryId)
                .authorId(authorId)
                .title("update title")
                .content("update content")
                .build();
    }

    private Inquiry getInquiry(Member author) {
        var inquiry = Inquiry.create(author, "title", "content");
        inquiryRepository.save(inquiry);
        return inquiry;
    }

    private Member getAuthor() {
        var author = MemberFixture.createMember();
        memberRepository.save(author);
        return author;
    }

}