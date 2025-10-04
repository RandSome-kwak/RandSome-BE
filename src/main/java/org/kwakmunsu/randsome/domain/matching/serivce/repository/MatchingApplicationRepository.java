package org.kwakmunsu.randsome.domain.matching.serivce.repository;

import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationListResponse;

public interface MatchingApplicationRepository {

    MatchingApplication save(MatchingApplication matchingApplication);
    MatchingApplication findById(Long id);

    // Admin 전용 메서드
    MatchingApplicationListResponse findAllByStatus(MatchingStatus status, int page);

}