package org.kwakmunsu.randsome.admin.member.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;

@Schema(description = "관리자용 회원 상세 응답 DTO")
@Builder
public record MemberDetailResponse(
        @Schema(description = "로그인 아이디", example = "kwakseobang")
        String loginId,

        @Schema(description = "실명", example = "곽문수")
        String legalName,

        @Schema(description = "닉네임", example = "랜덤곽")
        String nickname,

        @Schema(description = "성별 (예: 남자, 여자)", example = "남자")
        String gender,

        @Schema(description = "권한 (예: 회원, 매칭 후보, 관리자)", example = "회원")
        String role,

        @Schema(description = "MBTI (예: INTJ, ENFP)", example = "INTJ")
        Mbti mbti,

        @Schema(description = "인스타그램 아이디", example = "insta_id")
        String instagramId,

        @Schema(description = "자기소개", example = "안녕하세요! 만나서 반갑습니다.")
        String introduction,

        @Schema(description = "이상형 설명", example = "취미가 비슷한 사람을 좋아해요.")
        String idealDescription,

        @Schema(description = "생성 일시 (ISO-8601 형식)", example = "2025-10-01T12:34:56")
        LocalDateTime createdAt
) {

    public static MemberDetailResponse from(Member member) {
        return MemberDetailResponse.builder()
                .loginId(member.getLoginId())
                .legalName(member.getLegalName())
                .nickname(member.getNickname())
                .gender(member.getGender().getValue())
                .role(member.getRole().getValue())
                .mbti(member.getMbti())
                .instagramId(member.getInstagramId())
                .introduction(member.getIntroduction())
                .idealDescription(member.getIdealDescription())
                .createdAt(member.getCreatedAt())
                .build();
    }

}