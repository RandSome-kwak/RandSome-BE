package org.kwakmunsu.randsome.admin.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.admin.member.repository.dto.MemberListResponse;
import org.kwakmunsu.randsome.admin.member.service.MemberAdminService;
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

    @BeforeEach
    void setUp() {
        saveMember();
    }

    @DisplayName("관리자가 첫번째 페이지 회원 목록 조회를 한다.")
    @Test
    void getMembers() {
        // when
        MemberListResponse response = memberAdminService.getMemberList(1);

        // then
        assertThat(response.responses()).hasSize(20);
        assertThat(response.hasNext()).isTrue();
    }

    @DisplayName("관리자가 두번째 페이지 회원 목록 조회를 한다.")
    @Test
    void getMembersFromSec() {
        // when
        MemberListResponse response = memberAdminService.getMemberList(2);

        // then
        assertThat(response.responses()).hasSize(20);
    }

    @DisplayName("마지막 페이지 조회 시 hasNext가 false이다.")
    @Test
    void getLastPage() {
        // when
        MemberListResponse response = memberAdminService.getMemberList(5);

        // then
        assertThat(response.responses()).hasSize(20);
        assertThat(response.hasNext()).isFalse();
    }

    private void saveMember() {
        for (int i = 0; i < 100; i++) {
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