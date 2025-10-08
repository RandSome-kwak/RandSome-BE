package org.kwakmunsu.randsome.domain.matching.service.dto;

import lombok.Builder;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;

@Builder
public record MatchingApplicationServiceRequest(
        Long memberId,
        MatchingType type,
        int matchingCount
) {

}