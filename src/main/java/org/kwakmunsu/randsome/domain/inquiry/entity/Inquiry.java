package org.kwakmunsu.randsome.domain.inquiry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.randsome.domain.BaseEntity;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.global.exception.ConflictException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;

@Table(name = "inquiries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Inquiry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status", nullable = false)
    private InquiryStatus inquiryStatus;

    @Column(length = 5000)
    private String answer;

    public static Inquiry create(Member author, String title, String content) {
        Inquiry inquiry = new Inquiry();

        inquiry.author = author;
        inquiry.title = title;
        inquiry.content = content;
        inquiry.inquiryStatus = InquiryStatus.PENDING;
        inquiry.answer = null;

        return inquiry;
    }

    public void updateQuestion(String newTitle, String newContent) {
        if (this.inquiryStatus == InquiryStatus.COMPLETED) {
            throw new ConflictException(ErrorStatus.CANNOT_MODIFY_ANSWERED_INQUIRY);
        }

        this.title = newTitle;
        this.content = newContent;
    }

    public void registerAnswer(String newAnswer) {
        if (this.inquiryStatus == InquiryStatus.COMPLETED) {
            throw new ConflictException(ErrorStatus.CANNOT_MODIFY_ANSWER);
        }

        this.answer = newAnswer;
        completeAnswer();
    }

    public boolean isAnswered() {
        return this.inquiryStatus == InquiryStatus.COMPLETED;
    }


    private void completeAnswer() {
        this.inquiryStatus = InquiryStatus.COMPLETED;
    }

}