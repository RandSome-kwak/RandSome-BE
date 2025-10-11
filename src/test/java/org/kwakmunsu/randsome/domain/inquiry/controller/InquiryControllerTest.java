package org.kwakmunsu.randsome.domain.inquiry.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.randsome.global.security.annotation.TestMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class InquiryControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("문의를 등록 후 문의 id를 전달한다.")
    @Test
    void register() throws JsonProcessingException {
        // given
        var request = InquiryRegisterRequest.builder()
                .title("문의 제목")
                .content("문의 내용")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        given(inquiryService.registerInquiry(any())).willReturn(1L);

        // when
        assertThat(mvcTester.post().uri("/api/v1/inquiries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .hasStatus(HttpStatus.CREATED)
                .apply(print())
                .bodyText()
                .isEqualTo("1");
    }

}