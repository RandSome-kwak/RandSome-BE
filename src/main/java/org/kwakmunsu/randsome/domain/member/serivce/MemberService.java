package org.kwakmunsu.randsome.domain.member.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.dto.CheckResponse;
import org.kwakmunsu.randsome.domain.member.serivce.dto.MemberRegisterServiceRequest;
import org.kwakmunsu.randsome.global.exception.DuplicationException;
import org.kwakmunsu.randsome.global.exception.UnAuthenticationException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long register(MemberRegisterServiceRequest request) {
        checkedAlreadyLoginId(request.loginId());
        checkedAlreadyNickname(request.nickname());

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = request.toEntity(encodedPassword);

        return memberRepository.save(member).getId();
    }

    public Member getMember(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId);
        if (passwordEncoder.matches(password, member.getPassword())) {
            return member;
        }
        throw new UnAuthenticationException(ErrorStatus.INVALID_PASSWORD);
    }

    // ------------------- 중복 체크용 -------------------
    public CheckResponse isLoginIdAvailable(String loginId) {
        boolean available = !memberRepository.existsByLoginId(loginId);

        return new CheckResponse(available);
    }

    public CheckResponse isNicknameAvailable(String nickname) {
        boolean available = !memberRepository.existsByNickname(nickname);

        return new CheckResponse(available);
    }

    private void checkedAlreadyLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new DuplicationException(ErrorStatus.DUPLICATE_LOGIN_ID);
        }
    }

    private void checkedAlreadyNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicationException(ErrorStatus.DUPLICATE_NICKNAME);
        }
    }
}