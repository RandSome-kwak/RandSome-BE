package org.kwakmunsu.randsome.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.enums.Role;
import org.kwakmunsu.randsome.domain.member.service.MemberQueryService;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.kwakmunsu.randsome.global.jwt.JwtProvider;
import org.kwakmunsu.randsome.global.jwt.dto.TokenResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberQueryService memberQueryService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @DisplayName("로그인을 성공 시 토큰을 발급한다.")
    @Test
    void login() {
        // given
        var member = MemberFixture.createMember(1L);
        var tokenResponse = new TokenResponse("accessToken", "refreshToken");
        given(memberQueryService.getMember(any(String.class), any(String.class))).willReturn(member);
        given(jwtProvider.createTokens(any(Long.class), any(Role.class))).willReturn(tokenResponse);

        // when
        TokenResponse response = authService.login(member.getLoginId(), member.getPassword());

        // then
        assertThat(response)
                .extracting(TokenResponse::accessToken, TokenResponse::refreshToken)
                .containsExactly(tokenResponse.accessToken(), tokenResponse.refreshToken());

        assertThat(member.getRefreshToken()).isEqualTo(tokenResponse.refreshToken());
    }

    @DisplayName("존재하지 않은 사용자가 로그인 할 경우 예외가 발생한다.")
    @Test
    void failLogin() {
        // given
        given(memberQueryService.getMember(any(String.class), any(String.class)))
                .willThrow(new NotFoundException(ErrorStatus.NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> authService.login("login", "password"))
            .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("리프레시 토큰으로 토큰을 재발급한다.")
    @Test
    void reissue() {
        // given
        var member = MemberFixture.createMember(1L);
        var tokenResponse = new TokenResponse("new-accessToken", "new-refreshToken");
        given(memberRepository.findByRefreshToken(any(String.class))).willReturn(member);
        given(jwtProvider.createTokens(any(Long.class), any(Role.class))).willReturn(tokenResponse);
        // when
        TokenResponse response = authService.reissue("refreshToken");

        // then
        assertThat(response)
                .extracting(TokenResponse::accessToken, TokenResponse::refreshToken)
                .containsExactly(tokenResponse.accessToken(), tokenResponse.refreshToken());
        assertThat(member.getRefreshToken()).isEqualTo(tokenResponse.refreshToken());
    }

    @DisplayName("존재하지 않는 리프레시 토큰으로 토큰 재발급을 시도할 경우 예외가 발생한다.")
    @Test
    void failReissue() {
        // given
        given(memberRepository.findByRefreshToken(any(String.class)))
                .willThrow(new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER));

        // when & then
        assertThatThrownBy(() -> authService.reissue("invalid-refreshToken"))
            .isInstanceOf(NotFoundException.class);
    }

}