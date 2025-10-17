package org.kwakmunsu.randsome.admin.matching.service;

import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminListResponse;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;

public interface MatchingApplicationAdminRepository {

    MatchingApplication findById(Long id);

    MatchingApplicationAdminListResponse findAllByStatus(MatchingStatus status, int page);

    long countByStatus(MatchingStatus matchingStatus);
}