package org.kwakmunsu.randsome.admin.inquiry.serivce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.kwakmunsu.randsome.domain.inquiry.enums.InquiryState.PENDING;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryState;
import org.kwakmunsu.randsome.domain.inquiry.repository.dto.InquiryListAdminResponse;
import org.kwakmunsu.randsome.domain.inquiry.serivce.InquiryRepository;
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


    @DisplayName("관리자가 답변 대기 중인 문의 목록을 첫 페이지 조회 시 20개를 반환하고 다음 페이지가 있다.")
    @Test
    void getInquiresByPending() {
        // given
        int totalInquiries = 50;
        var author = MemberFixture.createMember();
        memberRepository.save(author);
        createAndSaveInquiries(totalInquiries, author);

        // when
        InquiryListAdminResponse response = inquiryAdminService.getInquires(PENDING, 1);

        // then
        assertThat(response.responses()).hasSize(20);  // 한 페이지 크기
        assertThat(response.hasNext()).isTrue();  // 다음 페이지 존재
        assertThat(response.totalCount()).isEqualTo(25L);  // 50개 중 25개가 PENDING (홀수 인덱스)

        // 모든 문의가 PENDING 상태인지 확인
        assertThat(response.responses())
                .allMatch(inquiry -> inquiry.state().equals(PENDING.getDescription()))
                .allMatch(inquiry -> inquiry.answer() == null);
    }

    @DisplayName("관리자가 답변 완료된 문의 목록을 조회한다.")
    @Test
    void getInquiresByCompleted() {
        // given
        int totalInquiries = 30;
        var author = MemberFixture.createMember();
        memberRepository.save(author);
        createAndSaveInquiries(totalInquiries, author);

        // when
        InquiryListAdminResponse response = inquiryAdminService.getInquires(InquiryState.COMPLETED, 1);

        // then
        assertThat(response.responses()).hasSize(15);  // 30개 중 15개가 COMPLETED (짝수 인덱스)
        assertThat(response.hasNext()).isFalse();  // 15개이므로 다음 페이지 없음
        assertThat(response.totalCount()).isEqualTo(15L);

        // 모든 문의가 COMPLETED 상태이고 답변이 있는지 확인
        assertThat(response.responses())
                .allMatch(inquiry -> inquiry.state().equals(InquiryState.COMPLETED.getDescription()))
                .allMatch(inquiry -> inquiry.answer() != null);
    }

    @DisplayName("관리자가 두 번째 페이지를 조회한다.")
    @Test
    void getInquiresSecondPage() {
        // given
        int totalInquiries = 50;
        var author = MemberFixture.createMember();
        memberRepository.save(author);
        createAndSaveInquiries(totalInquiries, author);

        // when
        InquiryListAdminResponse response = inquiryAdminService.getInquires(PENDING, 2);

        // then
        assertThat(response.responses()).hasSize(5);  // 25개 중 20개는 첫 페이지, 5개가 두 번째 페이지
        assertThat(response.hasNext()).isFalse();  // 마지막 페이지
        assertThat(response.totalCount()).isEqualTo(25L);
    }

    @DisplayName("관리자가 문의가 없는 상태를 조회하면 빈 목록을 반환한다.")
    @Test
    void getInquiresEmpty() {
        // given
        var author = MemberFixture.createMember();
        memberRepository.save(author);

        // when
        InquiryListAdminResponse response = inquiryAdminService.getInquires(PENDING, 1);

        // then
        assertThat(response.responses()).isEmpty();
        assertThat(response.hasNext()).isFalse();
        assertThat(response.totalCount()).isZero();
    }

    @DisplayName("관리자가 최신 문의가 먼저 조회되는지 확인한다.")
    @Test
    void getInquiresOrderByCreatedAtDesc() {
        // given
        Member author = MemberFixture.createMember();
        memberRepository.save(author);

        // 3개의 PENDING 문의 생성 (인덱스 1, 3, 5)
        for (int i = 0; i < 6; i++) {
            var inquiry = Inquiry.create(author, "title" + i, "content" + i);
            if (i % 2 == 0) {
                inquiry.registerAnswer("answer" + i);
            }
            inquiryRepository.save(inquiry);
        }

        // when
        InquiryListAdminResponse response = inquiryAdminService.getInquires(PENDING, 1);

        // then
        assertThat(response.responses()).hasSize(3);

        // 최신순 정렬 확인 (title5, title3, title1 순서)
        assertThat(response.responses())
                .extracting("title")
                .containsExactly("title5", "title3", "title1");
    }

    private void createAndSaveInquiries(int count, Member author) {
        for (int i = 0; i < count; i++) {
            var inquiry = Inquiry.create(author, "title" + i, "content" + i);
            if (i % 2 == 0) {
                inquiry.registerAnswer("answer" + i);
            }
            inquiryRepository.save(inquiry);
        }

    }

}