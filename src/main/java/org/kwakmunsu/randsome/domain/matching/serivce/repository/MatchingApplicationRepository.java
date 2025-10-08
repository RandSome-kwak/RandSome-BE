package org.kwakmunsu.randsome.domain.matching.serivce.repository;

import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.dto.AdminMatchingApplicationListResponse;

public interface MatchingApplicationRepository {

    MatchingApplication save(MatchingApplication matchingApplication);
    MatchingApplication findById(Long id);
    void findAllByRequesterIdAndStatus(Long requesterId, MatchingStatus status);

    // Admin 전용 메서드
    AdminMatchingApplicationListResponse findAllByStatus(MatchingStatus status, int page);

}