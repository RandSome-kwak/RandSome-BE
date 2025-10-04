package org.kwakmunsu.randsome.domain.matching.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.serivce.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingApplicationRepositoryImpl implements MatchingApplicationRepository {

    private final MatchingApplicationJpaRepository matchingApplicationJpaRepository;
    private final MatchingApplicationQueryDslRepository matchingApplicationQueryDslRepository;

    @Override
    public MatchingApplication save(MatchingApplication matchingApplication) {
        return matchingApplicationJpaRepository.save(matchingApplication);
    }

    @Override
    public MatchingApplication findById(Long id) {
        return matchingApplicationJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MATCHING_APPLICATION));
    }

    @Override
    public MatchingApplicationListResponse findAllByStatus(MatchingStatus status, int page) {
        return matchingApplicationQueryDslRepository.findAllByStatus(status, page);
    }

}