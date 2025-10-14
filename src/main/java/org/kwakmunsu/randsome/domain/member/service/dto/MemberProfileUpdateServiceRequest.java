package org.kwakmunsu.randsome.domain.member.service.dto;

import lombok.Builder;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;

@Builder
public record MemberProfileUpdateServiceRequest(
        Long memberId,
        String nickname,
        Mbti mbti,
        String instagramId,
        String introduction,
        String idealDescription
) {

}