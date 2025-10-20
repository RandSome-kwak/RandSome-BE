package org.kwakmunsu.randsome.admin.inquiry.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.inquiry.controller.dto.AnswerRegisterRequest;
import org.kwakmunsu.randsome.admin.inquiry.repository.dto.InquiryReadAdminResponse;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.global.security.annotation.TestAdmin;
import org.springframework.http.MediaType;

class InquiryAdminControllerTest extends ControllerTestSupport {

    @TestAdmin
    @DisplayName("관리자가 문의에 답변을 등록한다.")
    @Test
    void registerAnswer() throws JsonProcessingException {
        // given
        var request = new AnswerRegisterRequest("answer");
        var requestJson = objectMapper.writeValueAsString(request);

        // when
        assertThat(mvcTester.patch().uri("/api/v1/admin/inquiries/{inquiryId}/answers", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .hasStatusOk()
                .apply(print());

        // then
        verify(inquiryAdminService).registerAnswer(1L, request.answer());
    }

    @TestAdmin
    @DisplayName("관리자가 문의 목록을 조회한다.")
    @Test
    void getInquires() {
        // given
        List<InquiryReadAdminResponse> responses = getInquiryReadAdminResponse();

        Member member = MemberFixture.createMember();
        List<Inquiry> inquiries = List.of(Inquiry.create(member, "title", "content"));
        PageResponse<InquiryReadAdminResponse> response = new PageResponse<>(
                inquiries.stream()
                        .map(InquiryReadAdminResponse::from)
                        .toList(),
                1L
        );

        given(inquiryAdminService.getInquires(any(InquiryStatus.class), any(PageRequest.class))).willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/inquiries")
                .contentType(MediaType.APPLICATION_JSON)
                .param("status", InquiryStatus.PENDING.name()))
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.content", v -> v.assertThat().isNotNull())
                .hasPathSatisfying("$.count", v -> v.assertThat().isEqualTo(response.count().intValue()));
    }

    private List<InquiryReadAdminResponse> getInquiryReadAdminResponse() {
        return List.of(
                InquiryReadAdminResponse.builder()
                        .inquiryId(1L)
                        .authorId(1L)
                        .authorNickname("nickname")
                        .title("title")
                        .content("content")
                        .inquiryStatus(InquiryStatus.PENDING.getDescription())
                        .createdAt(LocalDateTime.now())
                        .build(),

                InquiryReadAdminResponse.builder()
                        .inquiryId(2L)
                        .authorId(1L)
                        .authorNickname("nickname")
                        .title("title")
                        .content("content")
                        .answer("answer")
                        .inquiryStatus(InquiryStatus.COMPLETED.getDescription())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

}