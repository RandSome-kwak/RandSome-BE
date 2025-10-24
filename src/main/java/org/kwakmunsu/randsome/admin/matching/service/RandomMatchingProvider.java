package org.kwakmunsu.randsome.admin.matching.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.candidate.repository.CandidateAdminRepository;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RandomMatchingProvider implements MatchingProvider {

    private final CandidateAdminRepository candidateRepository;

    @Override
    public MatchingType getType() {
        return MatchingType.RANDOM_MATCHING;
    }

    // 인원 수가 많지 않으므로, DB 에서 모두 조회 후 메모리에서 랜덤으로 추출
    // 추후 인원이 많아지면, DB 에서 랜덤 추출하는 방식으로 변경 필요
    @Override
    public List<Member> match(Member requester, int count) {
        List<Candidate> all = candidateRepository.findByGenderAndCandidateStatusAndStatus(requester.getGender(), CandidateStatus.APPROVED, EntityStatus.ACTIVE);
        List<Member> members = new ArrayList<>(all.stream()
                .map(Candidate::getMember)
                .toList());
        Collections.shuffle(members);

        return members.stream()
                .limit(count)
                .toList();
    }

}