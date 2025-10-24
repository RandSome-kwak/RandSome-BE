package org.kwakmunsu.randsome.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.randsome.domain.matching.service.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.matching.service.MatchingRepository;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberActivityStatsResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberActivityStatsService {

    private final MatchingApplicationRepository matchingApplicationRepository;
    private final MatchingRepository matchingRepository;
    private final InquiryRepository inquiryRepository;

    public MemberActivityStatsResponse getMemberActivityInfo(Long memberId) {
        long appliedCount = matchingApplicationRepository.countByRequesterIdAndStatus(memberId, EntityStatus.ACTIVE);
        long selectedCount = matchingRepository.countBySelectedMemberIdAndStatus(memberId, EntityStatus.ACTIVE);
        long inquiryCount = inquiryRepository.countByAuthorIdAndStatus(memberId, EntityStatus.ACTIVE);

        return new MemberActivityStatsResponse(appliedCount, selectedCount, inquiryCount);
    }

}