package org.kwakmunsu.randsome.admin.inquiry.serivce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryState;
import org.kwakmunsu.randsome.domain.inquiry.serivce.InquiryRepository;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.kwakmunsu.randsome.global.exception.ConflictException;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class InquiryAdminServiceIntegrationTest {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InquiryAdminService inquiryAdminService;


    @DisplayName("관리지가 문의에 답변을 등록한다.")
    @Test
    void registerAnswer() {
        // given
        var author = MemberFixture.createMember();
        memberRepository.save(author);

        var inquiry = Inquiry.create(author, "title", "content");
        inquiryRepository.save(inquiry);
        var answer = "new Answer";

        // when
        inquiryAdminService.registerAnswer(inquiry.getId(), answer);

        // then
        var result = inquiryRepository.findById(inquiry.getId());

        assertThat(result).extracting(
                        Inquiry::getAnswer,
                        Inquiry::isAnswered,
                        Inquiry::getState
                )
                .containsExactly(
                        answer,
                        true,
                        InquiryState.COMPLETED
                );
    }

    @DisplayName("존재하지 않는 문의 내역에 답변 등록 요청 시 예외가 발생한다.")
    @Test
    void failRegisterAnswerWhenNotExistInquiry() {
        // given
        var invalidInquiryId = 999L;
        var answer = "new Answer";

        // when & then
        assertThatThrownBy(() -> inquiryAdminService.registerAnswer(invalidInquiryId, answer))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이미 답변이 등록된 문의에 다시 답변을 등록하면 예외가 발생한다.")
    @Test
    void failRegisterAnswer() {
        // given
        var author = MemberFixture.createMember();
        memberRepository.save(author);

        var inquiry = Inquiry.create(author, "title", "content");
        inquiryRepository.save(inquiry);
        var answer = "new Answer";

        inquiryAdminService.registerAnswer(inquiry.getId(), answer);

        // when & then
        assertThatThrownBy(() -> inquiryAdminService.registerAnswer(inquiry.getId(), answer))
                .isInstanceOf(ConflictException.class);
    }


}