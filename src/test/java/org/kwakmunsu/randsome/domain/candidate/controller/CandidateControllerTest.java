package org.kwakmunsu.randsome.domain.candidate.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.global.security.annotation.TestMember;
import org.springframework.http.MediaType;

class CandidateControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("후보자 등록에 성공한다.")
    @Test
    void register() {
        // when
        assertThat(mvcTester.post().uri("/api/v1/matching/candidates")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk();

        // then
        verify(candidateService).register(anyLong());
    }

}