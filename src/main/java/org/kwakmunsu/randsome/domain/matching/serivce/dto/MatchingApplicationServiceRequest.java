package org.kwakmunsu.randsome.domain.matching.serivce.dto;

import lombok.Builder;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;

@Builder
public record MatchingApplicationServiceRequest(
        Long memberId,
        MatchingType type,
        int matchingCount
) {

}