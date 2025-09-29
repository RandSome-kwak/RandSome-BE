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
import org.kwakmunsu.randsome.domain.member.serivce.dto.CheckResponse;
import org.kwakmunsu.randsome.domain.member.serivce.dto.MemberProfileResponse;
import org.kwakmunsu.randsome.global.exception.DuplicationException;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
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

    @DisplayName("로그인 아이디 중복 체크를 한다")
    @Test
    void checkLogin() {
        // given
        given(memberRepository.existsByLoginId(any(String.class))).willReturn(false);

        // when
        CheckResponse response = memberService.isLoginIdAvailable("test");

        // then
        assertThat(response.available()).isTrue();
    }

    @DisplayName("닉네임 중복 체크를 한다")
    @Test
    void checkNickname() {
        // given
        given(memberRepository.existsByNickname(any(String.class))).willReturn(false);

        // when
        CheckResponse response = memberService.isNicknameAvailable("test");

        // then
        assertThat(response.available()).isTrue();
    }

    @DisplayName("본인 프로필을 조회한다.")
    @Test
    void getProfile() {
        // given
        var member = MemberFixture.createMember();
        given(memberRepository.findById(any(Long.class))).willReturn(member);

        // when
        MemberProfileResponse response = memberService.getProfile(1L);

        // then
        assertThat(response).extracting(
                        MemberProfileResponse::legalName,
                        MemberProfileResponse::nickname,
                        MemberProfileResponse::gender,
                        MemberProfileResponse::role,
                        MemberProfileResponse::mbti,
                        MemberProfileResponse::introduction,
                        MemberProfileResponse::idealDescription,
                        MemberProfileResponse::instagramId
                )
                .containsExactly(
                        member.getLegalName(),
                        member.getNickname(),
                        member.getGender().getValue(),
                        member.getRole().getValue(),
                        member.getMbti(),
                        member.getIntroduction(),
                        member.getIdealDescription(),
                        member.getInstagramId()
                );
    }

    @DisplayName("존재하지 않는 회원의 프로필 조회를 시도하면 예외가 발생한다.")
    @Test
    void failGetProfile() {
        // given
        given(memberRepository.findById(any(Long.class))).willThrow(new NotFoundException(ErrorStatus.NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> memberService.getProfile(1L))
                .isInstanceOf(NotFoundException.class);

    }

}