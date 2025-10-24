package org.kwakmunsu.randsome.domain.statistics.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.repository.CandidateRepository;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.statistics.service.dto.MatchingStatisticsResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final CandidateRepository candidateRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;

    public MatchingStatisticsResponse getMatchingStatistics() {
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusNanos(1);

        long totalCandidateCount = candidateRepository.countByCandidateStatusAndStatus(CandidateStatus.APPROVED, EntityStatus.ACTIVE);
        long todayMatchingCount = matchingApplicationRepository.countByCreatedAtBetween(startOfToday, endOfToday);
        long totalMatchingCount = matchingApplicationRepository.countByMatchingStatus(MatchingStatus.COMPLETED);

        return MatchingStatisticsResponse.of(totalCandidateCount, todayMatchingCount, totalMatchingCount);
    }

}