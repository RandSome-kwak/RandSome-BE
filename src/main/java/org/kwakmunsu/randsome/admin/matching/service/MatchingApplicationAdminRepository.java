package org.kwakmunsu.randsome.admin.matching.service;

import java.util.List;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminPreviewResponse;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;

public interface MatchingApplicationAdminRepository {

    MatchingApplication findById(Long id);

    List<MatchingApplicationAdminPreviewResponse> findAllByMatchingStatusAndStatus(MatchingStatus matchingStatus, int offset, int page,
            EntityStatus status);

    long countByMatchingStatusAndStatus(MatchingStatus matchingStatus, EntityStatus status);
}