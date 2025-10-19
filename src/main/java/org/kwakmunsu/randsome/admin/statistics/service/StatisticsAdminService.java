package org.kwakmunsu.randsome.admin.statistics.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.candidate.serivce.CandidateAdminRepository;
import org.kwakmunsu.randsome.admin.matching.service.MatchingApplicationAdminRepository;
import org.kwakmunsu.randsome.admin.member.service.MemberAdminRepository;
import org.kwakmunsu.randsome.admin.statistics.service.dto.MatchingStatisticsAdminResponse;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticsAdminService {

    private final MemberAdminRepository memberRepository;
    private final CandidateAdminRepository candidateAdminRepository;
    private final MatchingApplicationAdminRepository matchingApplicationRepository;
    private final StatisticsRepository statisticsRepository;

    public MatchingStatisticsAdminResponse getMatchingStatistics() {
        long totalMemberCount = memberRepository.count();
        long totalCandidateCount = candidateAdminRepository.countByCandidateStatus(CandidateStatus.APPROVED);
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