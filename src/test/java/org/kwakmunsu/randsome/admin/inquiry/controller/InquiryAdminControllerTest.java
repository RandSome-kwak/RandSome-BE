package org.kwakmunsu.randsome.admin.inquiry.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.admin.inquiry.controller.dto.AnswerRegisterRequest;
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

}