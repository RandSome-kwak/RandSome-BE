package org.kwakmunsu.randsome.admin.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberListResponse;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberPreviewResponse;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberAdminServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberAdminService memberAdminService;

    @DisplayName("관리자가 회원 상세 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var member = MemberFixture.createMember();
        given(memberRepository.findById(any(Long.class))).willReturn(member);

        // when
        MemberDetailResponse response = memberAdminService.getMember(1L);

        // then
        assertThat(response).extracting(
                MemberDetailResponse:: loginId,
                MemberDetailResponse:: legalName,
                MemberDetailResponse:: nickname,
                MemberDetailResponse:: role,
                MemberDetailResponse:: gender,
                MemberDetailResponse:: mbti,
                MemberDetailResponse:: introduction,
                MemberDetailResponse:: idealDescription,
                MemberDetailResponse:: introduction,
                MemberDetailResponse:: instagramId,
                MemberDetailResponse:: createdAt
                )
                .containsExactly(
                        member.getLoginId(),
                        member.getLegalName(),
                        member.getNickname(),
                        member.getRole().getValue(),
                        member.getGender().getValue(),
                        member.getMbti(),
                        member.getIntroduction(),
                        member.getIdealDescription(),
                        member.getIntroduction(),
                        member.getInstagramId(),
                        member.getCreatedAt()
                );
    }

    @DisplayName("관리자가 회원 목록을 조회한다.")
    @Test
    void getMembers() {
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
        MemberListResponse mockResponse = MemberListResponse.builder()
                .responses(List.of(member1, member2))
                .hasNext(false)
                .totalCount(2L)
                .build();
        given(memberRepository.findAll(anyInt())).willReturn(mockResponse);

        // when
        MemberListResponse response = memberAdminService.getMembers(1);

        // then
        assertThat(response).isNotNull();
        assertThat(response.responses()).hasSize(2);
        assertThat(response.hasNext()).isFalse();
        assertThat(response.totalCount()).isEqualTo(2L);
    }

}