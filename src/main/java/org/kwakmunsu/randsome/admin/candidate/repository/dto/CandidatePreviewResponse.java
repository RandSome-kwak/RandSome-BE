package org.kwakmunsu.randsome.admin.candidate.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.member.entity.Member;

@Schema(description = "매칭 후보자 신청 목록 정보 DTO")
@Builder
public record CandidatePreviewResponse(
        @Schema(description = "매칭 후보자 신청 ID", example = "1")
        Long candidateId,

        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "실명", example = "김랜덤")
        String legalName,

        @Schema(description = "닉네임", example = "랜덤맨")
        String nickname,

        @Schema(description = "성별", example = "남자")
        String gender,

        @Schema(description = "신청일시", example = "2023-10-05T14:30:00")
        LocalDateTime appliedAt,

        @Schema(description = "승인 상태", example = "대기 | 승인 | 거절")
        String status
) {

    public static CandidatePreviewResponse from(Candidate candidate) {
        Member candidateMember = candidate.getMember();

        return CandidatePreviewResponse.builder()
                .candidateId(candidate.getId())
                .memberId(candidateMember.getId())
                .legalName(candidateMember.getLegalName())
                .nickname(candidateMember.getNickname())
                .gender(candidateMember.getGender().getValue())
                .appliedAt(candidate.getCreatedAt())
                .status(candidate.getCandidateStatus().getDescription())
                .build();
    }

}