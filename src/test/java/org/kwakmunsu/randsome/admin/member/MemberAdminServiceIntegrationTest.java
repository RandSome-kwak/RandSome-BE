package org.kwakmunsu.randsome.admin.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.member.repository.dto.MemberListResponse;
import org.kwakmunsu.randsome.admin.member.service.MemberAdminService;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberAdminServiceIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberAdminService memberAdminService;

    private static final int TOTAL_COUNT = 15;
    @BeforeEach
    void setUp() {
        saveMember();
    }

    @DisplayName("관리자가 첫번째 페이지 회원 목록 조회를 한다.")
    @Test
    void getMembers() {
        // when
        PageResponse<MemberDetailResponse> response = memberAdminService.getMembers(new PageRequest(1));

        // then
        assertThat(response.content()).hasSize(10);
        assertThat(response.count()).isEqualTo(TOTAL_COUNT);
    }

    @DisplayName("관리자가 두번째 페이지 회원 목록 조회를 한다.")
    @Test
    void getMembersFromSec() {
        // when
        PageResponse<MemberDetailResponse> response = memberAdminService.getMembers(new PageRequest(2));

        // then
        assertThat(response.content()).hasSize(5);
        assertThat(response.count()).isEqualTo(TOTAL_COUNT);
    }


    private void saveMember() {
        for (int i = 0; i < TOTAL_COUNT; i++) {
            Gender gender;
            if (i % 2 == 0) gender = Gender.M;
            else gender = Gender.F;

            Member member = Member.createMember(
                    "loginId" + i,
                    "password" + i,
                    "legalName" + i,
                    "nickname" + i,
                    gender,
                    Mbti.ENFJ,
                    "instagramId" + i,
                    "introduction" + i,
                    "idealDescription" + i
            );
            memberRepository.save(member);
        }
    }

}