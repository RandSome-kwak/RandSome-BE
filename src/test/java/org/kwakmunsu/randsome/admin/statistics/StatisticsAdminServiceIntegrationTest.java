package org.kwakmunsu.randsome.admin.statistics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.admin.statistics.repository.StatisticsRepository;
import org.kwakmunsu.randsome.admin.statistics.service.StatisticsAdminService;
import org.kwakmunsu.randsome.admin.statistics.service.dto.MatchingStatisticsAdminResponse;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.repository.CandidateRepository;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.service.InquiryRepository;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class StatisticsAdminServiceIntegrationTest {

    @Autowired
    private StatisticsAdminService statisticsAdminService;

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private MatchingApplicationRepository matchingApplicationRepository;


    @DisplayName("매칭 통계를 조회한다.")
    @Test
    void getMatchingStatistics() {
        // given
        var member = MemberFixture.createMember();
        memberRepository.save(member);

        var candidate = Candidate.create(member);
        candidate.approve();
        candidateRepository.save(candidate);

        var matchingApplication = MatchingApplication.create(member, MatchingType.IDEAL_MATCHING, 3);
        var matchingApplicationSec = MatchingApplication.create(member, MatchingType.RANDOM_MATCHING, 4);
        matchingApplication.complete();
        matchingApplicationRepository.save(matchingApplication);
        matchingApplicationRepository.save(matchingApplicationSec);

        Inquiry inquiry = Inquiry.create(member, "title", "content");
        inquiry.delete();
        inquiryRepository.save(inquiry);

        // when
        MatchingStatisticsAdminResponse response = statisticsAdminService.getMatchingStatistics();

        // then
        assertThat(response).extracting(
                        MatchingStatisticsAdminResponse::totalMemberCount,
                        MatchingStatisticsAdminResponse::totalCandidateCount,
                        MatchingStatisticsAdminResponse::totalMatchingCount,
                        MatchingStatisticsAdminResponse::pendingApprovalsCount
                )
                .containsExactly(
                        1L,
                        1L,
                        1L,
                        1L
                );
    }

}