package org.kwakmunsu.randsome.domain.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberRegisterRequest;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.serivce.dto.CheckResponse;
import org.kwakmunsu.randsome.domain.member.serivce.dto.MemberRegisterServiceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberControllerTest extends ControllerTestSupport {

    @DisplayName("회원 가입 요청 후 가입에 성공한다")
    @Test
    void register() throws JsonProcessingException {
        // given
        var request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        given(memberService.register(any(MemberRegisterServiceRequest.class))).willReturn(1L);

        // when & then
        assertThat(mvcTester.post().uri("/api/v1/members/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatus(HttpStatus.CREATED)
                .bodyText()
                .isEqualTo("1");
    }

    @DisplayName("회원 등록 API 요청 할 때 잘못된 요청값이 존재할 경우 400 에러가 발생한다")
    @ParameterizedTest
    @CsvSource({
            "'', 'password123!', 'legalName', 'nickname', 'M', 'INTJ', 'instaId', 'intro', 'ideal'",        // loginId empty
            "'login123', '', legalName', 'nickname', 'M', 'INTJ', 'instaId', 'intro', 'ideal'",            // password empty
            "'login123', 'short', legalName','nickname', 'M', 'INTJ', 'instaId', 'intro', 'ideal'",       // password too short
            "'login123', 'password123!', '', 'nickname', 'M', 'INTJ', 'instaId', 'intro', 'ideal'",        // legalName empty
            "'login123', 'password123!', legalName', '', 'M', 'INTJ', 'instaId', 'intro', 'ideal'",        // nickname empty
            "'login123', 'password123!', legalName', 'nickname', '', 'INTJ', 'instaId', 'intro', 'ideal'", // gender null
            "'login123', 'password123!', legalName', 'nickname', 'M', '', 'instaId', 'intro', 'ideal'",    // mbti null
            "'login123', 'password123!', legalName', 'nickname', 'M', 'INTJ', '', 'intro', 'ideal'",       // instagramId empty
            "'login123', 'password123!', legalName', 'nickname', 'M', 'INTJ', 'instaId', '', 'ideal'",     // introduction empty
            "'login123', 'password123!', legalName', 'nickname', 'M', 'INTJ', 'instaId', 'intro', ''"      // idealDescription empty
    })
    void registerMemberWithInvalidRequest(
            String loginId,
            String password,
            String legalName,
            String nickname,
            String gender,
            String mbti,
            String instagramId,
            String introduction,
            String idealDescription
    ) throws Exception {

        // enum 변환: 빈 문자열이면 null로
        Gender genderEnum = (gender == null || gender.isBlank()) ? null : Gender.valueOf(gender);
        Mbti mbtiEnum = (mbti == null || mbti.isBlank()) ? null : Mbti.valueOf(mbti);

        // given
        var request = new MemberRegisterRequest(
                loginId,
                password,
                legalName,
                nickname,
                genderEnum,
                mbtiEnum,
                instagramId,
                introduction,
                idealDescription
        );
        var requestJson = objectMapper.writeValueAsString(request);

        // when & then
        assertThat(mvcTester.post().uri("/api/v1/members/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .apply(print())
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("로그인 아이디 중복 체크를 한다")
    @Test
    void checkLoginId() {
        // given
        var loginId = "testLoginId";
        given(memberService.isLoginIdAvailable(loginId)).willReturn(new CheckResponse(true));

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/members/check-login-id")
                .param("loginId", loginId))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.available").isEqualTo(true);
    }

    @DisplayName("닉네임 중복 체크를 한다")
    @Test
    void checkNickname() {
        // given
        var nickname = "testNickname";
        given(memberService.isNicknameAvailable(nickname)).willReturn(new CheckResponse(true));

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/members/check-nickname")
                .param("nickname", nickname))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.available").isEqualTo(true);
    }

}