package org.kwakmunsu.randsome.admin.member.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Role;

@Schema(description = "관리자용 회원 목록 정보 DTO")
@Builder
public record MemberPreviewResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,

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

        @Schema(description = "생성 일시 (ISO-8601 형식)", example = "2025-10-01T12:34:56")
        LocalDateTime createdAt
) {
    // QueryDSL용 enum을 받는 생성자
    public MemberPreviewResponse(
            Long memberId,
            String loginId,
            String legalName,
            String nickname,
            Gender genderEnum,
            Role roleEnum,
            LocalDateTime createdAt
    ) {
        this(
                memberId,
                loginId,
                legalName,
                nickname,
                genderEnum.getValue(),
                roleEnum.getValue(),
                createdAt
        );
    }

}
