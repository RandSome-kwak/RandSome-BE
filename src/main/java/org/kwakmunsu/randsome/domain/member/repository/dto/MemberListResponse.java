package org.kwakmunsu.randsome.domain.member.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationPreviewResponse;

@Schema(description = "회원 미리보기 응답 리스트 DTO")
@Builder
public record MemberListResponse(
        @Schema(description = "회원 미리보기 응답 리스트")
        List<MemberPreviewResponse> responses,

        @Schema(description = "다음 페이지 존재 여부", example = "true | false")
        boolean hasNext
) {

}