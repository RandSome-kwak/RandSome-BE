package org.kwakmunsu.randsome.admin.candidate.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.admin.candidate.controller.dto.CandidateStatusUpdateRequest;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.global.security.annotation.TestAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CandidateAdminControllerTest extends ControllerTestSupport {

    // 어드민 전용 어노테이션 만드렁야함
    @TestAdmin
    @DisplayName("관리자가 후보자 요청을 승인한다.")
    @Test
    void approve() throws JsonProcessingException {
        // given
        var request = new CandidateStatusUpdateRequest(CandidateStatus.APPROVED);
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        assertThat(mvcTester.put().uri("/api/v1/admin/candidates/{candidateId}/status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .hasStatusOk()
                .apply(print());

        verify(candidateAdminService).updateCandidateStatus(1L, CandidateStatus.APPROVED);
    }

    @TestAdmin
    @DisplayName("유효하지 않은 상태 값으로 후보자 요청을 승인/거절할 수 없다.")
    @Test
    void failApprove() {
        // given
        String invalidRequestJson = "{\"status\":\"INVALID_STATUS\"}";

        // when
        assertThat(mvcTester.put().uri("/api/v1/admin/candidates/{candidateId}/status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .apply(print());
    }

}