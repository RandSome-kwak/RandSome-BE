package org.kwakmunsu.randsome.admin.candidate.serivce;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidatePreviewResponse;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CandidateAdminService {

    private final CandidateAdminRepository candidateAdminRepository;

    // 관리자용 후보자 승인/거절 기능
    @Transactional
    public void updateCandidateStatus(Long candidateId, CandidateStatus status) {
        Candidate candidate = candidateAdminRepository.findByIdWithMember(candidateId);

        if (status == CandidateStatus.APPROVED) {
            approveCandidate(candidate);
        } else if (status == CandidateStatus.REJECTED) {
            candidate.reject();
        }

        log.info("[Candidate Status Updated] candidateId= {}, newStatus= {}", candidateId, status);
    }

    @Transactional(readOnly = true)
    public PageResponse<CandidatePreviewResponse> getCandidates(CandidateStatus status, PageRequest request) {
        List<Candidate> candidates = candidateAdminRepository.findAllByCandidateStatus(status, request.offset(), request.limit());
        long count = candidateAdminRepository.countByCandidateStatusAndStatus(status, EntityStatus.ACTIVE);

        return new PageResponse<>(candidates.stream()
                .map(CandidatePreviewResponse::from)
                .toList(),
                count
        );
    }

    private void approveCandidate(Candidate candidate) {
        candidate.approve();
        Member candidateMember = candidate.getMember();
        candidateMember.updateRoleToCandidate();
    }

}