package org.kwakmunsu.randsome.admin.statistics.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.statistics.service.dto.MatchingStatisticsResponse;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.serivce.CandidateRepository;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticsAdminService {

    private final MemberRepository memberRepository;
    private final CandidateRepository candidateRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final StatisticsRepository statisticsRepository;

    public MatchingStatisticsResponse getMatchingStatistics() {
        long totalMemberCount = memberRepository.count();
        long totalCandidateCount = candidateRepository.countByStatus(CandidateStatus.APPROVED);
        long totalMatchingCount = matchingApplicationRepository.countByStatus(MatchingStatus.COMPLETED);

        // 승인 대기 수 (후보, 매칭, 문의 승인 대기 수)
        String pending = "PENDING";
        long pendingApprovals = statisticsRepository.findPendingApprovals(pending);

        return MatchingStatisticsResponse.of(
                totalMemberCount,
                totalCandidateCount,
                totalMatchingCount,
                pendingApprovals
        );
    }

}