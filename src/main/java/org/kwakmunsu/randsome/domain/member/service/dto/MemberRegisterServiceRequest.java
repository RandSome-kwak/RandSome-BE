package org.kwakmunsu.randsome.domain.member.service.dto;

import lombok.Builder;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;

@Builder
public record MemberRegisterServiceRequest(
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

    public Member toEntity(String encodedPassword) {
        return Member.createMember(
                loginId,
                encodedPassword,
                legalName,
                nickname,
                gender,
                mbti,
                instagramId,
                introduction,
                idealDescription
        );
    }

}