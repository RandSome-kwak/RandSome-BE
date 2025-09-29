package org.kwakmunsu.randsome.domain.candidate.serivce;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.kwakmunsu.randsome.global.exception.DuplicationException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CandidateService {

    private final MemberRepository memberRepository;
    private final CandidateRepository candidateRepository;

    public void register(Long memberId) {
        Optional<Candidate> optionalCandidate = candidateRepository.findByMemberId(memberId);
        // 이미 등록되었거나 승인 대기 중이라면 등록 불가
        optionalCandidate.ifPresent(this::validateCandidateRegistration);

        // 신규 등록
        Member member = memberRepository.findById(memberId);
        Candidate candidate = Candidate.create(member);
        candidateRepository.save(candidate);
    }

    private void validateCandidateRegistration(Candidate candidate) {
        switch (candidate.getStatus()) {
            case APPROVED:
                throw new DuplicationException(ErrorStatus.ALREADY_APPROVED);
            case PENDING:
                throw new DuplicationException(ErrorStatus.PENDING_MEMBER);
            case REJECTED:
                // 거절 상태는 다시 등록 가능하므로 통과
            default:
                break;
        }
    }

}