package org.kwakmunsu.randsome.admin.statistics.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.candidate.repository.CandidateAdminRepository;
import org.kwakmunsu.randsome.admin.matching.repository.matchingapplication.MatchingApplicationAdminRepository;
import org.kwakmunsu.randsome.admin.member.service.MemberAdminRepository;
import org.kwakmunsu.randsome.admin.statistics.service.dto.MatchingStatisticsAdminResponse;
import org.kwakmunsu.randsome.domain.EntityStatus;
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
        long totalMemberCount = memberRepository.countByStatus(EntityStatus.ACTIVE);
        long totalCandidateCount = candidateAdminRepository.countByCandidateStatusAndStatus(CandidateStatus.APPROVED, EntityStatus.ACTIVE);
        long totalMatchingCount = matchingApplicationRepository.countByMatchingStatusAndStatus(MatchingStatus.COMPLETED,
                EntityStatus.ACTIVE);

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