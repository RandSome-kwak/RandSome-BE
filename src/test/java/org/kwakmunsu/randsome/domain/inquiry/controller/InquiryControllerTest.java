package org.kwakmunsu.randsome.domain.inquiry.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.randsome.domain.inquiry.controller.dto.InquiryUpdateRequest;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryListResponse;
import org.kwakmunsu.randsome.domain.inquiry.service.dto.InquiryReadResponse;
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

        given(inquiryCommandService.register(any())).willReturn(1L);

        // when
        assertThat(mvcTester.post().uri("/api/v1/inquiries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .hasStatus(HttpStatus.CREATED)
                .apply(print())
                .bodyText()
                .isEqualTo("1");
    }

    @TestMember
    @DisplayName("자신의 문의 내역을 조회한다.")
    @Test
    void getInquires() {
        // given
        var inquiryCount = 10;
        var inquiryReadResponses = createInquiryReadResponse(inquiryCount);
        var inquiryListResponse = new InquiryListResponse(inquiryReadResponses);
        given(inquiryQueryService.getInquires(any(Long.class))).willReturn(inquiryListResponse);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/inquiries")
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.inquiries", inquiries ->
                        assertThat(inquiries).asList().hasSize(inquiryCount));
    }

    @TestMember
    @DisplayName("자신의 문의를 수정한다.")
    @Test
    void update() throws JsonProcessingException {
        // given
        var request = InquiryUpdateRequest.builder()
                .title("수정된 문의 제목")
                .content("수정된 문의 내용")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        assertThat(mvcTester.patch().uri("/api/v1/inquiries/{inquiryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatusOk();

        // then
        verify(inquiryCommandService).update(any());
    }

    @TestMember
    @DisplayName("문의 내역을 삭제한다.")
    @Test
    void delete() {
        // when
        assertThat(mvcTester.delete().uri("/api/v1/inquiries/{inquiryId}", 1L))
                .apply(print())
                .hasStatus(HttpStatus.NO_CONTENT);

        // then
        verify(inquiryCommandService).delete(1L, 1L);
    }

    private List<InquiryReadResponse> createInquiryReadResponse(int count) {
        List<InquiryReadResponse> inquiryReadResponses = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            InquiryReadResponse inquiryReadResponse = InquiryReadResponse.builder()
                    .inquiryId(i)
                    .authorNickname("작성자 " + i)
                    .title("문의 제목 " + i)
                    .content("문의 내용 " + i)
                    .createdAt(LocalDateTime.now())
                    .build();
            inquiryReadResponses.add(inquiryReadResponse);
        }
        return inquiryReadResponses;
    }

}