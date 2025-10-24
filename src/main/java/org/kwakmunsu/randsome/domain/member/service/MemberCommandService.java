package org.kwakmunsu.randsome.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.repository.MemberRepository;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberProfileUpdateServiceRequest;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberRegisterServiceRequest;
import org.kwakmunsu.randsome.global.exception.ConflictException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long register(MemberRegisterServiceRequest request) {
        checkedAlreadyLoginId(request.loginId());
        checkedAlreadyNickname(request.nickname());
        checkedAlreadyInstagramId(request.instagramId());

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = request.toEntity(encodedPassword);

        return memberRepository.save(member).getId();
    }

    @Transactional
    public void updateProfile(MemberProfileUpdateServiceRequest request) {
        Member member = memberRepository.findById(request.memberId());

        // 닉네임이 변경되었을 때만 중복 체크
        if (!member.hasNickname(request.nickname())) {
            checkedAlreadyNickname(request.nickname());
        }

        member.updateInfo(
                request.nickname(),
                request.mbti(),
                request.instagramId(),
                request.introduction(),
                request.idealDescription()
        );
    }

    private void checkedAlreadyLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new ConflictException(ErrorStatus.DUPLICATE_LOGIN_ID);
        }
    }

    private void checkedAlreadyNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new ConflictException(ErrorStatus.DUPLICATE_NICKNAME);
        }
    }

    private void checkedAlreadyInstagramId(String instagramId) {
        if (memberRepository.existsByInstagramId(instagramId)) {
            throw new ConflictException(ErrorStatus.DUPLICATE_INSTAGRAM);
        }
    }

}