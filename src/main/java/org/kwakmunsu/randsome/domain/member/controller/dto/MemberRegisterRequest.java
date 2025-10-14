package org.kwakmunsu.randsome.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberRegisterServiceRequest;

@Schema(description = "회원 가입 요청 DTO")
public record MemberRegisterRequest(
        @Schema(description = "로그인 아이디", example = "randsome123")
        @NotBlank(message = "로그인 아이디는 필수 입력 값입니다.")
        String loginId,

        @Schema(description = "비밀번호", example = "password123!")
        @Size(min = 8)
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        String password,

        @Schema(description = "법적 이름", example = "김랜덤썸")
        @Size(min = 1, max = 100)
        @NotBlank(message = "법적 이름은 필수 입력 값입니다.")
        String legalName,

        @Schema(description = "닉네임", example = "randsome")
        @Size(min = 1, max = 100)
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        String nickname,

        @Schema(description = "성별", example = "M: 남자, F: 여자")
        @NotNull(message = "성별은 필수 입력 값입니다.")
        Gender gender,

        @Schema(description = "MBTI", example = "INTJ")
        @NotNull(message = "MBTI는 필수 입력 값입니다.")
        Mbti mbti,

        @Schema(description = "인스타그램 아이디", example = "mununu_su")
        @Size(min = 1, max = 100)
        @NotBlank(message = "인스타그램 아이디는 필수 입력 값입니다.")
        String instagramId,

        @Schema(description = "자기소개", example = "안녕하세요, 랜덤썸입니다.")
        @Size(min = 1, max = 1000)
        @NotBlank(message = "자기소개는 필수 입력 값입니다.")
        String introduction,

        @Schema(description = "이상형 소개", example = "친절하고 유머러스한 사람")
        @Size(min = 1, max = 1000)
        @NotBlank(message = "이상형 소개는 필수 입력 값입니다.")
        String idealDescription
) {

    public MemberRegisterServiceRequest toServiceRequest() {
        return MemberRegisterServiceRequest.builder()
                .loginId(loginId)
                .password(password)
                .legalName(legalName)
                .nickname(nickname)
                .gender(gender)
                .mbti(mbti)
                .instagramId(instagramId)
                .introduction(introduction)
                .idealDescription(idealDescription)
                .build();
    }

}