package org.kwakmunsu.randsome.admin.matching.repository.matchingapplication;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminPreviewResponse;
import org.kwakmunsu.randsome.admin.matching.service.MatchingApplicationAdminRepository;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingApplicationAdminRepositoryImpl implements MatchingApplicationAdminRepository {

    private final MatchingApplicationAdminJpaRepository matchingApplicationAdminJpaRepository;
    private final MatchingApplicationQueryDslRepository matchingApplicationQueryDslRepository;

    @Override
    public MatchingApplication findById(Long id) {
        return matchingApplicationAdminJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MATCHING_APPLICATION));
    }

    @Override
    public List<MatchingApplicationAdminPreviewResponse> findAllByMatchingStatusAndStatus(MatchingStatus matchingStatus, int offset, int limit, EntityStatus status) {
        return matchingApplicationQueryDslRepository.findAllByMatchingStatusAndStatus(matchingStatus, offset, limit, status);
    }

    @Override
    public long countByMatchingStatusAndStatus(MatchingStatus matchingStatus, EntityStatus status) {
        return matchingApplicationAdminJpaRepository.countByMatchingStatusAndStatus(matchingStatus, status);
    }

}