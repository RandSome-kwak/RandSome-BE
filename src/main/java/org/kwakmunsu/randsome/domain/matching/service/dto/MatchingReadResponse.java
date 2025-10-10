package org.kwakmunsu.randsome.domain.matching.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;

@Schema(description = "매칭 정보 응답 DTO")
@Builder
public record MatchingReadResponse(
        @Schema(description = "매칭 유형", example = "랜덤 | 이상형")
        String matchingType,

        @Schema(description = "요청한 매칭 수", example = "3")
        int requestedCount,

        @Schema(description = "매칭 신청 일시", example = "2023-10-05T14:48:00")
        LocalDateTime requestedAt,

        @Schema(description = "매칭된 회원 정보")
        List<MatchingMemberResponse> memberResponse
) {

    public static MatchingReadResponse of(MatchingApplication application, List<MatchingMemberResponse> memberResponses) {
        return MatchingReadResponse.builder()
                .matchingType(application.getMatchingType().getDescription())
                .requestedCount(application.getRequestedCount())
                .requestedAt(application.getCreatedAt())
                .memberResponse(memberResponses)
                .build();
    }

}