package org.kwakmunsu.randsome.domain.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberProfileUpdateRequest;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberRegisterRequest;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.service.dto.CheckResponse;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberActivityStatsResponse;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberProfileResponse;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberProfileUpdateServiceRequest;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberRegisterServiceRequest;
import org.kwakmunsu.randsome.global.security.annotation.TestMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberControllerTest extends ControllerTestSupport {

    @DisplayName("회원 가입 요청 후 가입에 성공한다")
    @Test
    void register() throws JsonProcessingException {
        // given
        var request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        given(memberCommandService.register(any(MemberRegisterServiceRequest.class))).willReturn(1L);

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
            "'login123', 'password123!', legalName', 'nickname', 'M', 'INTJ', 'instaId', 'intro', ''"
            // idealDescription empty
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

        given(memberQueryService.isLoginIdAvailable(loginId)).willReturn(new CheckResponse(true));

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

        given(memberQueryService.isNicknameAvailable(nickname)).willReturn(new CheckResponse(true));

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/members/check-nickname")
                .param("nickname", nickname))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.available").isEqualTo(true);
    }

    @DisplayName("인스타그램 ID 중복 체크를 한다")
    @Test
    void checkInstagramId() {
        // given
        var instagramId = "testInstagramId";

        given(memberQueryService.isInstagramIdAvailable(instagramId)).willReturn(new CheckResponse(true));

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/members/check-instagram-id")
                .param("instagramId", instagramId))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.available").isEqualTo(true);
    }

    @TestMember
    @DisplayName("내 프로필을 조회한다")
    @Test
    void getProfile() {
        // given
        Member member = MemberFixture.createMember();
        var response = MemberProfileResponse.from(member);

        given(memberQueryService.getProfile(any(Long.class))).willReturn(response);
        // when & then
        assertThat(mvcTester.get().uri("/api/v1/members/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.legalName", v -> v.assertThat().isEqualTo(response.legalName()))
                .hasPathSatisfying("$.nickname", v -> v.assertThat().isEqualTo(response.nickname()))
                .hasPathSatisfying("$.gender", v -> v.assertThat().isEqualTo(response.gender()))
                .hasPathSatisfying("$.role", v -> v.assertThat().isEqualTo(response.role()))
                .hasPathSatisfying("$.mbti", v -> v.assertThat().isEqualTo(response.mbti().name()))
                .hasPathSatisfying("$.introduction", v -> v.assertThat().isEqualTo(response.introduction()))
                .hasPathSatisfying("$.idealDescription", v -> v.assertThat().isEqualTo(response.idealDescription()))
                .hasPathSatisfying("$.instagramId", v -> v.assertThat().isEqualTo(response.instagramId()));
    }

    @TestMember
    @DisplayName("회원 프로필 정보를 업데이트 한다.")
    @Test
    void updateProfile() throws JsonProcessingException {
        // given
        var request = new MemberProfileUpdateRequest(
                "newNickname",
                Mbti.ENFJ,
                "newInsta",
                "newIntro",
                "newIdeal"
        );
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        assertThat(mvcTester.patch().uri("/api/v1/members/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatusOk();

        // then
        verify(memberCommandService).updateProfile(any(MemberProfileUpdateServiceRequest.class));
    }

    @TestMember
    @DisplayName("회원 활동 통계 정보를 조회한다")
    @Test
    void getActivity() {
        // given
        MemberActivityStatsResponse response = new MemberActivityStatsResponse(5L, 5L, 5L);
        given(memberQueryService.getMemberActivityInfo(any(Long.class))).willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/members/activity-stats")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.appliedCount", v -> v.assertThat().isEqualTo(5))
                .hasPathSatisfying("$.selectedCount", v -> v.assertThat().isEqualTo(5))
                .hasPathSatisfying("$.inquiryCount", v -> v.assertThat().isEqualTo(5));
    }

}