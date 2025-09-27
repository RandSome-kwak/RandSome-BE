package org.kwakmunsu.randsome.domain.auth.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberService;
import org.kwakmunsu.randsome.global.jwt.JwtProvider;
import org.kwakmunsu.randsome.global.jwt.dto.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Transactional
    public TokenResponse login(String loginId, String password) {
        Member member = memberService.getMember(loginId, password);

        TokenResponse tokens = jwtProvider.createTokens(member.getId(), member.getRole());

        member.updateRefreshToken(tokens.refreshToken());
        return tokens;
    }

}