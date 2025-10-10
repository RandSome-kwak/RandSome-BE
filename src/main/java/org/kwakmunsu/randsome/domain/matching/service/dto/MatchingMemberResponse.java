package org.kwakmunsu.randsome.domain.matching.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;

@Schema(description = "매칭된 회원 정보 응답 DTO")
@Builder
public record MatchingMemberResponse(
        String nickname,
        Mbti mbti,
        String instagramId,
        String introduction
) {

    public static MatchingMemberResponse from(Member member) {
        return MatchingMemberResponse.builder()
                .nickname(member.getNickname())
                .mbti(member.getMbti())
                .instagramId(member.getInstagramId())
                .introduction(member.getIntroduction())
                .build();
    }

}