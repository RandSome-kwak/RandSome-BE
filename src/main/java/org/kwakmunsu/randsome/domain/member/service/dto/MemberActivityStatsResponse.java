package org.kwakmunsu.randsome.domain.member.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Member Activity Stats Response DTO")
public record MemberActivityStatsResponse(

        @Schema(description = "본인 매칭 신청 횟수", example = "5")
        long appliedCount,

        @Schema(description = "본인이 후보자로 보여진 횟수", example = "3")
        long selectedCount,

        @Schema(description = "본인 문의 횟수", example = "10")
        long inquiryCount
) {

}