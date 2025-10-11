package org.kwakmunsu.randsome.domain.inquiry.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryState;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.global.exception.ConflictException;

class InquiryTest {

    Member author = MemberFixture.createMember();
    String title = "문의 제목";
    String content = "문의 내용";
    Inquiry inquiry;

    @BeforeEach
    void setUp() {
        inquiry = Inquiry.create(author, title, content);
    }

    @DisplayName("문의를 생성한다.")
    @Test
    void create() {
        // then
        assertThat(inquiry).extracting(
                        Inquiry::getAuthor,
                        Inquiry::getTitle,
                        Inquiry::getContent,
                        Inquiry::getState,
                        Inquiry::getAnswer
                )
                .containsExactly(
                        author,
                        title,
                        content,
                        InquiryState.PENDING,
                        null
                );
    }

    @DisplayName("문의 내용을 수정한다.")
    @Test
    void updateQuestion() {
        // given
        var newTitle = "수정된 문의 제목";
        var newContent = "수정된 문의 내용";
        assertThat(inquiry).extracting(Inquiry::getTitle, Inquiry::getContent)
                .containsExactly(title, content);
        // when
        inquiry.updateQuestion(newTitle, newContent);

        // then
        assertThat(inquiry).extracting(Inquiry::getTitle, Inquiry::getContent)
                .containsExactly(newTitle, newContent);
    }

    @DisplayName("문의에 답변을 추가한다")
    @Test
    void updateAnswer() {
        // given
        var newAnswer = "답변 내용";

        assertThat(inquiry).extracting(Inquiry::getState, Inquiry::getAnswer)
                .containsExactly(InquiryState.PENDING, null);
        // when
        inquiry.registerAnswer(newAnswer);

        // then
        assertThat(inquiry).extracting(Inquiry::getState, Inquiry::getAnswer)
                .containsExactly(InquiryState.COMPLETED, newAnswer);
    }

    @DisplayName("답변이 완료된 문의의 내용을 수정할 수 없다.")
    @Test
    void failUpdateQuestion() {
        // given
        var newAnswer = "답변 내용";
        inquiry.registerAnswer(newAnswer);

        // when & then
        assertThatThrownBy(() -> inquiry.updateQuestion("수정된 제목", "수정된 내용"))
                .isInstanceOf(ConflictException.class);
    }

    @DisplayName("답변이 완료된 문의는 답변을 추가할 수 없다.")
    @Test
    void failUpdateAnswer() {
        // given
        var newAnswer = "답변 내용";
        inquiry.registerAnswer(newAnswer);

        // when & then
        assertThatThrownBy(() -> inquiry.registerAnswer("수정된 답변"))
                .isInstanceOf(ConflictException.class);
    }

}