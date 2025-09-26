package org.kwakmunsu.randsome.domain.member.serivce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.global.exception.DuplicationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("회원 가입에 성공한다.")
    @Test
    void register() {
        // given
        var member = MemberFixture.createMember(1L);
        var request = MemberFixture.createMemberRegisterRequest();

        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(memberRepository.existsByLoginId(any(String.class))).willReturn(false);
        given(memberRepository.existsByNickname(any(String.class))).willReturn(false);

        // when
        Long memberId = memberService.register(request.toServiceRequest());

        // then
        assertThat(memberId).isEqualTo(member.getId());
    }

    @DisplayName("닉네임이 중복되어 회원 가입에 실패한다")
    @Test
    void failRegisterWhenAlreadyExistsNickname() {
        // given
        var request = MemberFixture.createMemberRegisterRequest();

        given(memberRepository.existsByLoginId(any(String.class))).willReturn(false);
        given(memberRepository.existsByNickname(any(String.class))).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.register(request.toServiceRequest()))
                .isInstanceOf(DuplicationException.class);
    }

    @DisplayName("로그인 아이디가 중복되어 회원 가입에 실패한다")
    @Test
    void failRegisterWhenAlreadyExistsLoginId() {
        // given
        var request = MemberFixture.createMemberRegisterRequest();

        given(memberRepository.existsByLoginId(any(String.class))).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.register(request.toServiceRequest()))
                .isInstanceOf(DuplicationException.class);
    }

}