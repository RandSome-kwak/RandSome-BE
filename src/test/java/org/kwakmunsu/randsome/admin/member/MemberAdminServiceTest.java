package org.kwakmunsu.randsome.admin.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
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

}