package org.kwakmunsu.randsome.admin.matching.repository.matchingapplication;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.matching.service.MatchingApplicationAdminRepository;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminListResponse;
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
    public MatchingApplicationAdminListResponse findAllByStatus(MatchingStatus status, int page) {
        return matchingApplicationQueryDslRepository.findAllByStatus(status, page);
    }

    @Override
    public long countByStatus(MatchingStatus matchingStatus) {
        return matchingApplicationAdminJpaRepository.countByStatus(matchingStatus);
    }

}