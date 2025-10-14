package org.kwakmunsu.randsome.domain.member.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;

@Schema(description = "프로필 응답 DTO")
@Builder
public record MemberProfileResponse(
        @Schema(description = "실명", example = "홍길동")
        String legalName,

        @Schema(description = "닉네임", example = "길동이")
        String nickname,

        @Schema(description = "성별 (수정 불가)", example = "남자 | 여자")
        String gender,

        @Schema(description = "역할 (수정 불가)", example = "회원 | 매칭 후보 ")
        String role,

        @Schema(description = "MBTI 유형", example = "INTJ")
        Mbti mbti,

        @Schema(description = "자기소개", example = "안녕하세요, 자기소개입니다.")
        String introduction,

        @Schema(description = "이상형 설명", example = "이상형은 친절한 사람입니다.")
        String idealDescription,

        @Schema(description = "인스타그램 아이디", example = "hong_gildong")
        String instagramId
) {

    public static MemberProfileResponse from(Member member) {
        return MemberProfileResponse.builder()
                .legalName(member.getLegalName())
                .nickname(member.getNickname())
                .gender(member.getGender().getValue())
                .role(member.getRole().getValue())
                .mbti(member.getMbti())
                .introduction(member.getIntroduction())
                .idealDescription(member.getIdealDescription())
                .instagramId(member.getInstagramId())
                .build();
    }

}