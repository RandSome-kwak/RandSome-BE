package org.kwakmunsu.randsome.admin.candidate.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.admin.candidate.serivce.dto.CandidateListReadServiceRequest;
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
    public CandidateListResponse getCandidates(CandidateListReadServiceRequest request) {
        return candidateAdminRepository.findAllByStatus(request.status(), request.page());
    }

    private void approveCandidate(Candidate candidate) {
        candidate.approve();
        Member candidateMember = candidate.getMember();
        candidateMember.updateRoleToCandidate();
    }

}