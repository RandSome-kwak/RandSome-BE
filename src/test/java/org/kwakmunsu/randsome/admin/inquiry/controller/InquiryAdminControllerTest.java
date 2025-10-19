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
import org.kwakmunsu.randsome.admin.inquiry.controller.dto.AnswerRegisterRequest;
import org.kwakmunsu.randsome.admin.inquiry.repository.dto.InquiryListAdminResponse;
import org.kwakmunsu.randsome.admin.inquiry.repository.dto.InquiryReadAdminResponse;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
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

        var response = InquiryListAdminResponse.builder()
                .responses(responses)
                .hasNext(false)
                .totalCount((long) responses.size())
                .build();

        given(inquiryAdminService.getInquires(any(InquiryStatus.class), any(Integer.class))).willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/inquiries")
                .contentType(MediaType.APPLICATION_JSON)
                .param("status", InquiryStatus.PENDING.name()))
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.responses[0].inquiryId", v -> v.assertThat().isEqualTo(responses.getFirst().inquiryId().intValue()))
                .hasPathSatisfying("$.responses[0].authorId", v -> v.assertThat().isEqualTo(responses.getFirst().authorId().intValue()))
                .hasPathSatisfying("$.responses[0].authorNickname", v -> v.assertThat().isEqualTo(responses.getFirst().authorNickname()))
                .hasPathSatisfying("$.responses[0].title", v -> v.assertThat().isEqualTo(responses.getFirst().title()))
                .hasPathSatisfying("$.responses[0].content", v -> v.assertThat().isEqualTo(responses.getFirst().content()))
                .hasPathSatisfying("$.responses[0].answer", v -> v.assertThat().isEqualTo(responses.getFirst().answer()))
                .hasPathSatisfying("$.responses[0].inquiryStatus", v -> v.assertThat().isEqualTo(responses.getFirst().inquiryStatus()))
                .hasPathSatisfying("$.responses[0].createdAt", v -> v.assertThat().isEqualTo(responses.getFirst().createdAt().toString()))
                .hasPathSatisfying("$.hasNext", v -> v.assertThat().isEqualTo(false))
                .hasPathSatisfying("$.totalCount", v -> v.assertThat().isEqualTo(response.totalCount().intValue()));
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