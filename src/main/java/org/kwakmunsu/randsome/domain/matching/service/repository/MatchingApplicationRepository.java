package org.kwakmunsu.randsome.domain.matching.service.repository;

import java.util.List;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.dto.AdminMatchingApplicationListResponse;

public interface MatchingApplicationRepository {

    MatchingApplication save(MatchingApplication matchingApplication);
    MatchingApplication findById(Long id);
    List<MatchingApplication> findAllByRequesterIdAndStatus(Long requesterId, MatchingStatus status);
    List<MatchingApplication> findAllByRequesterIdAndStatusIn(Long requesterId, List<MatchingStatus> statuses);

    // Admin 전용 메서드
    AdminMatchingApplicationListResponse findAllByStatus(MatchingStatus status, int page);

}