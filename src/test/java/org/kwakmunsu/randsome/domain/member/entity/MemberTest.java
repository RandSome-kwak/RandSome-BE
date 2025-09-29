package org.kwakmunsu.randsome.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.enums.Role;

class MemberTest {

    @DisplayName("멤버 권한의 회원을 생성한다")
    @Test
    void createMember() {
        // given
        var loginId = "testLoginId";
        var password = "testPassword";
        var legalName = "TestLegalName";
        var nickname = "testNickname";
        var gender = Gender.M;
        var mbti = Mbti.ENFP;
        var instagramId = "testInsta";
        var introduction = "Hello, I'm a test member.";
        var idealDescription = "Looking for someone special.";

        // when
        var member = Member.createMember(loginId, password, legalName, nickname, gender, mbti, instagramId, introduction, idealDescription);

        // then
        assertThat(member)
                .extracting(
                        Member::getLoginId,
                        Member::getPassword,
                        Member::getNickname,
                        Member::getGender,
                        Member::getRole,
                        Member::getMbti,
                        Member::getInstagramId,
                        Member::getIntroduction,
                        Member::getIdealDescription
                ).containsExactly(
                        loginId,
                        password,
                        nickname,
                        gender,
                        Role.ROLE_MEMBER,
                        mbti,
                        instagramId,
                        introduction,
                        idealDescription
                );
    }

    @DisplayName("어드민 권한의 회원을 생성한다")
    @Test
    void createAdmin() {
        // given
        var loginId = "testLoginId";
        var password = "testPassword";
        var nickname = "testAdminNickname";

        // when
        var admin = Member.createAdmin(loginId, password, nickname);

        // then
        assertThat(admin)
                .extracting(
                        Member::getLoginId,
                        Member::getPassword,
                        Member::getNickname,
                        Member::getGender,
                        Member::getRole,
                        Member::getMbti,
                        Member::getInstagramId,
                        Member::getIntroduction,
                        Member::getIdealDescription
                ).containsExactly(
                        loginId,
                        password,
                        nickname,
                        Gender.ADMIN,
                        Role.ROLE_ADMIN,
                        Mbti.ADMIN,
                        "admin",
                        "관리자입니다.",
                        "관리자입니다."
                );
    }

    @DisplayName("회원 정보를 업데이트 한다.")
    @Test
    void updateInfo() {
        // given
        var loginId = "testLoginId";
        var password = "testPassword";
        var legalName = "TestLegalName";
        var nickname = "testNickname";
        var gender = Gender.M;
        var mbti = Mbti.ENFP;
        var instagramId = "testInsta";
        var introduction = "Hello, I'm a test member.";
        var idealDescription = "Looking for someone special.";

        var member = Member.createMember(loginId, password, legalName, nickname, gender, mbti, instagramId, introduction, idealDescription);

        var newNickname = "updatedNickname";
        var newMbti = Mbti.INTJ;
        var newInstagramId = "updatedInsta";

        // when
        member.updateInfo(newNickname, newMbti, newInstagramId, introduction, idealDescription);

        // then
        assertThat(member)
                .extracting(
                        Member::getNickname,
                        Member::getGender,
                        Member::getRole,
                        Member::getMbti,
                        Member::getInstagramId,
                        Member::getIntroduction,
                        Member::getIdealDescription
                ).containsExactly(
                        newNickname,
                        gender,
                        Role.ROLE_MEMBER,
                        newMbti,
                        newInstagramId,
                        introduction,
                        idealDescription
                );
    }

    @DisplayName("매칭 후보자로 등록을 하여 회원 상태를 후보자로 변경한다.")
    @Test
    void updateRoleToCandidate() {
        // given
        var loginId = "testLoginId";
        var password = "testPassword";
        var legalName = "TestLegalName";
        var nickname = "testNickname";
        var gender = Gender.M;
        var mbti = Mbti.ENFP;
        var instagramId = "testInsta";
        var introduction = "Hello, I'm a test member.";
        var idealDescription = "Looking for someone special.";

        var member = Member.createMember(loginId, password, legalName, nickname, gender, mbti, instagramId, introduction, idealDescription);

        // when
        member.updateRoleToCandidate();

        // then
        assertThat(member.getRole()).isEqualTo(Role.ROLE_CANDIDATE);
    }

    @DisplayName("비밀번호를 변경한다.")
    @Test
    void changePassword() {
        // given
        var loginId = "testLoginId";
        var password = "testPassword";
        var legalName = "TestLegalName";
        var nickname = "testNickname";
        var gender = Gender.M;
        var mbti = Mbti.ENFP;
        var instagramId = "testInsta";
        var introduction = "Hello, I'm a test member.";
        var idealDescription = "Looking for someone special.";

        var member = Member.createMember(loginId, password, legalName, nickname, gender, mbti, instagramId, introduction, idealDescription);

        var newPassword = "newTestPassword";

        // when
        member.changePassword(newPassword);

        // then
        assertThat(member.getPassword()).isEqualTo(newPassword);

    }
}