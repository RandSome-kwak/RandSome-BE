package org.kwakmunsu.randsome.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.repository.MemberRepository;
import org.kwakmunsu.randsome.domain.member.service.dto.CheckResponse;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberActivityStatsResponse;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberProfileResponse;
import org.kwakmunsu.randsome.global.exception.UnAuthenticationException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberActivityStatsService memberActivityStatsService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member getMember(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId);
        if (isPasswordMatches(password, member)) {
            return member;
        }
        throw new UnAuthenticationException(ErrorStatus.INVALID_PASSWORD);
    }

    public MemberProfileResponse getProfile(Long memberId) {
        Member member = memberRepository.findById(memberId);

        return MemberProfileResponse.from(member);
    }

    public MemberActivityStatsResponse getMemberActivityInfo(Long memberId) {
        return memberActivityStatsService.getMemberActivityInfo(memberId);
    }

    public CheckResponse isLoginIdAvailable(String loginId) {
        boolean available = !memberRepository.existsByLoginId(loginId);

        return new CheckResponse(available);
    }

    public CheckResponse isNicknameAvailable(String nickname) {
        boolean available = !memberRepository.existsByNickname(nickname);

        return new CheckResponse(available);
    }

    public  CheckResponse isInstagramIdAvailable(String instagramId) {
        boolean available = !memberRepository.existsByInstagramId(instagramId);

        return new CheckResponse(available);
    }

    private boolean isPasswordMatches(String password, Member member) {
        return passwordEncoder.matches(password, member.getPassword());
    }

}