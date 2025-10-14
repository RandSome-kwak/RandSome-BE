package org.kwakmunsu.randsome.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.serivce.dto.MemberProfileUpdateServiceRequest;

@Schema(description = "회원 프로필 수정 요청 DTO")
@Builder
public record MemberProfileUpdateRequest(
        @Schema(description = "닉네임", example = "랜덤맨")
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        String nickname,

        @Schema(description = "MBTI", example = "INTJ")
        @NotNull(message = "MBTI는 필수 입력 값입니다.")
        Mbti mbti,

        @Schema(description = "인스타그램 ID", example = "randomman_insta")
        @NotBlank(message = "인스타그램 ID는 필수 입력 값입니다.")
        String instagramId,

        @Schema(description = "자기소개", example = "안녕하세요! 랜덤맨입니다.")
        @NotBlank(message = "자기소개는 필수 입력 값입니다.")
        String introduction,

        @Schema(description = "이상형 소개", example = "유머러스하고 따뜻한 마음을 가진 사람을 좋아해요.")
        @NotBlank(message = "이상형 소개는 필수 입력 값입니다.")
        String idealDescription
) {

    public MemberProfileUpdateServiceRequest toServiceRequest(Long memberId) {
        return MemberProfileUpdateServiceRequest.builder()
                .memberId(memberId)
                .nickname(nickname)
                .mbti(mbti)
                .instagramId(instagramId)
                .introduction(introduction)
                .idealDescription(idealDescription)
                .build();
    }

}