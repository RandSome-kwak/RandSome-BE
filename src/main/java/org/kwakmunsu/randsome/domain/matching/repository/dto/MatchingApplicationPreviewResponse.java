package org.kwakmunsu.randsome.domain.matching.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.member.enums.Gender;

@Schema(description = "매칭 신청 목록 정보 DTO")
public record MatchingApplicationPreviewResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "실명", example = "김랜덤")
        String legalName,

        @Schema(description = "닉네임", example = "랜덤맨")
        String nickname,

        @Schema(description = "성별", example = "남자")
        String gender,

        @Schema(description = "매칭 유형", example = "랜덤 | 이상형")
        String matchingType,

        @Schema(description = "매칭 인원", example = "2")
        int matchingCount,

        @Schema(description = "가격", example = "1000")
        BigDecimal price,

        @Schema(description = "신청일시", example = "2023-10-05T14:30:00")
        LocalDateTime appliedAt,

        @Schema(description = "승인 상태", example = "대기 | 승인 | 거절")
        String status
) {

    // QueryDSL용 enum을 받는 생성자 (새로 추가)
    public MatchingApplicationPreviewResponse(
            Long memberId,
            String legalName,
            String nickname,
            Gender genderEnum,
            MatchingType matchingTypeEnum,
            int matchingCount,
            BigDecimal price,
            LocalDateTime appliedAt,
            MatchingStatus statusEnum
    ) {
        this(
                memberId,
                legalName,
                nickname,
                genderEnum.getValue(),    // 변환해서 저장
                matchingTypeEnum.getDescription(),
                matchingCount,
                price,
                appliedAt,
                statusEnum.getDescription()     // 변환해서 저장
        );
    }

}