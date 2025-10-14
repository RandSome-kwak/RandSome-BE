package org.kwakmunsu.randsome.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.domain.member.service.MemberService;
import org.kwakmunsu.randsome.global.jwt.JwtProvider;
import org.kwakmunsu.randsome.global.jwt.dto.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;


    @Transactional
    public TokenResponse login(String loginId, String password) {
        Member member = memberService.getMember(loginId, password);

        TokenResponse tokens = jwtProvider.createTokens(member.getId(), member.getRole());

        member.updateRefreshToken(tokens.refreshToken());
        return tokens;
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken);
        TokenResponse tokens = jwtProvider.createTokens(member.getId(), member.getRole());

        member.updateRefreshToken(tokens.refreshToken());
        return tokens;
    }

}