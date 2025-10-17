package org.kwakmunsu.randsome.admin.statistics.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.candidate.serivce.CandidateAdminRepository;
import org.kwakmunsu.randsome.admin.statistics.service.dto.MatchingStatisticsAdminResponse;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticsAdminService {

    private final MemberRepository memberRepository;
    private final CandidateAdminRepository candidateAdminRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final StatisticsRepository statisticsRepository;

    public MatchingStatisticsAdminResponse getMatchingStatistics() {
        long totalMemberCount = memberRepository.count();
        long totalCandidateCount = candidateAdminRepository.countByStatus(CandidateStatus.APPROVED);
        long totalMatchingCount = matchingApplicationRepository.countByStatus(MatchingStatus.COMPLETED);

        // 승인 대기 수 (후보, 매칭, 문의 승인 대기 수)
        String pending = "PENDING";
        long pendingApprovals = statisticsRepository.findPendingApprovals(pending);

        return MatchingStatisticsAdminResponse.of(
                totalMemberCount,
                totalCandidateCount,
                totalMatchingCount,
                pendingApprovals
        );
    }

}