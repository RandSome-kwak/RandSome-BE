package org.kwakmunsu.randsome.domain.member.entity;

import static org.kwakmunsu.randsome.domain.member.enums.Role.ROLE_ADMIN;
import static org.kwakmunsu.randsome.domain.member.enums.Role.ROLE_MEMBER;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.randsome.domain.BaseEntity;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.enums.Role;

@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String legalName;

    @Column(length = 100, nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mbti mbti;

    @Column(length = 100, nullable = false, unique = true)
    private String instagramId;

    @Column(length = 1000, nullable = false)
    private String introduction;

    @Column(length = 1000, nullable = false)
    private String idealDescription;

    @Column(unique = true)
    private String refreshToken;

    public static Member createMember(
            String loginId,
            String password,
            String legalName,
            String nickname,
            Gender gender,
            Mbti mbti,
            String instagramId,
            String introduction,
            String idealDescription
    ) {
        Member member = new Member();

        member.loginId = loginId;
        member.password = password;
        member.legalName = legalName;
        member.nickname = nickname;
        member.gender = gender;
        member.role = ROLE_MEMBER;
        member.mbti = mbti;
        member.instagramId = instagramId;
        member.introduction = introduction;
        member.idealDescription = idealDescription;
        member.refreshToken = null;

        return member;
    }

    public static Member createAdmin(String loginId, String password, String nickname) {
        Member admin = new Member();
        admin.loginId = loginId;
        admin.password = password;
        admin.legalName = "관리자";
        admin.nickname = nickname;
        admin.gender = Gender.ADMIN;
        admin.role = ROLE_ADMIN;
        admin.mbti = Mbti.ADMIN;
        admin.instagramId = "admin";
        admin.introduction = "관리자입니다.";
        admin.idealDescription = "관리자입니다.";
        admin.refreshToken = null;

        return admin;
    }

    public void updateInfo(
            String nickname,
            Mbti mbti,
            String instagramId,
            String introduction,
            String idealDescription
    ) {
        this.nickname = nickname;
        this.mbti = mbti;
        this.instagramId = instagramId;
        this.introduction = introduction;
        this.idealDescription = idealDescription;
    }

    public void updateRoleToCandidate() {
        this.role = Role.ROLE_CANDIDATE;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean hasNickname(String nickname) {
        return this.nickname.equals(nickname);
    }

}