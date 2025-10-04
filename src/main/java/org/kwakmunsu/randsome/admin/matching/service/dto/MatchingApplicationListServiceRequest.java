package org.kwakmunsu.randsome.admin.matching.service.dto;

import lombok.Builder;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;

@Builder
public record MatchingApplicationListServiceRequest(MatchingStatus status, int page) {

}