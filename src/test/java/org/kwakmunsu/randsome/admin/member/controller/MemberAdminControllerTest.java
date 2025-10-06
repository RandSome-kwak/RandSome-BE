package org.kwakmunsu.randsome.admin.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberListResponse;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberPreviewResponse;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.global.security.annotation.TestAdmin;
import org.springframework.http.MediaType;


class MemberAdminControllerTest extends ControllerTestSupport {

    @TestAdmin
    @DisplayName("관리자가 회원 정보를 조회한다.")
    @Test
    void getMemberProfile() {
        // given
        MemberDetailResponse response = MemberDetailResponse.builder()
                .loginId("testuser")
                .legalName("테스트유저")
                .nickname("테스트닉네임")
                .gender("남자")
                .role("회원")
                .mbti(Mbti.INTJ)
                .instagramId("test_insta")
                .introduction("테스트 자기소개")
                .idealDescription("테스트 이상형")
                .createdAt(LocalDateTime.now())
                .build();
        given(memberAdminService.getMember(any(Long.class))).willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/members/{memberId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.loginId", v -> v.assertThat().isEqualTo(response.loginId()))
                .hasPathSatisfying("$.legalName", v -> v.assertThat().isEqualTo(response.legalName()))
                .hasPathSatisfying("$.nickname", v -> v.assertThat().isEqualTo(response.nickname()))
                .hasPathSatisfying("$.gender", v -> v.assertThat().isEqualTo(response.gender()))
                .hasPathSatisfying("$.role", v -> v.assertThat().isEqualTo(response.role()))
                .hasPathSatisfying("$.mbti", v -> v.assertThat().isEqualTo(response.mbti().name()))
                .hasPathSatisfying("$.instagramId", v -> v.assertThat().isEqualTo(response.instagramId()))
                .hasPathSatisfying("$.introduction", v -> v.assertThat().isEqualTo(response.introduction()))
                .hasPathSatisfying("$.idealDescription", v -> v.assertThat().isEqualTo(response.idealDescription()))
                .hasPathSatisfying("$.createdAt", v -> v.assertThat().isEqualTo(response.createdAt().toString()));
    }

    @TestAdmin
    @DisplayName("관리자가 회원 목록을 조회한다.")
    @Test
    void getMemberList() {
        // given
        LocalDateTime now = LocalDateTime.now();
        MemberPreviewResponse member1 = MemberPreviewResponse.builder()
                .memberId(1L)
                .loginId("testuser1")
                .legalName("테스트유저1")
                .nickname("테스트닉네임1")
                .gender("남자")
                .role("회원")
                .createdAt(now)
                .build();
        MemberPreviewResponse member2 = MemberPreviewResponse.builder()
                .memberId(2L)
                .loginId("testuser2")
                .legalName("테스트유저2")
                .nickname("테스트닉네임2")
                .gender("여자")
                .role("회원")
                .createdAt(now)
                .build();
        MemberListResponse response = MemberListResponse.builder()
                .responses(List.of(member1, member2))
                .hasNext(false)
                .totalCount(2L)
                .build();
        given(memberAdminService.getMembers(anyInt())).willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/members?page=1")
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.responses", v -> v.assertThat().isInstanceOf(List.class))
                .hasPathSatisfying("$.hasNext", v -> v.assertThat().isEqualTo(false))
                .hasPathSatisfying("$.totalCount", v -> v.assertThat().isEqualTo(2));
    }

}