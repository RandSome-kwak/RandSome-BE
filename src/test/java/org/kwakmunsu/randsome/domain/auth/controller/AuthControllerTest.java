package org.kwakmunsu.randsome.domain.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.domain.auth.controller.dto.LoginRequest;
import org.kwakmunsu.randsome.global.jwt.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AuthControllerTest extends ControllerTestSupport {

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() throws JsonProcessingException {
        // given
        var request = new LoginRequest("loginId", "password");
        var requestJson = objectMapper.writeValueAsString(request);
        var tokenResponse = new TokenResponse("accessToken", "refreshToken");

        given(authService.login(any(String.class), any(String.class))).willReturn(tokenResponse);

        // when & then
        assertThat(mvcTester.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .hasPathSatisfying("$.accessToken", v -> v.assertThat().isEqualTo(tokenResponse.accessToken()))
                .hasPathSatisfying("$.refreshToken", v -> v.assertThat().isEqualTo(tokenResponse.refreshToken()));
    }

}