package org.kwakmunsu.randsome.admin.member.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "관리자용 회원 목록 응답 DTO")
@Builder
public record MemberListResponse(
        @Schema(description = "회원 목록")
        List<MemberPreviewResponse> responses,

        @Schema(description = "다음 페이지 존재 여부", example = "true | false")
        boolean hasNext,

        @Schema(description = "총 회원 수", example = "100")
        Long totalCount
) {

}
