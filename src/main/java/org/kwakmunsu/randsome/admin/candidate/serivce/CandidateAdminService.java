package org.kwakmunsu.randsome.admin.candidate.serivce;

import static org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus.APPROVED;
import static org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus.REJECTED;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.admin.candidate.serivce.dto.CandidateListReadServiceRequest;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.domain.candidate.serivce.CandidateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CandidateAdminService {

    private final CandidateRepository candidateRepository;

    // 관리자용 후보자 승인/거절 기능
    @Transactional
    public void updateCandidateStatus(Long candidateId, CandidateStatus status) {
        Candidate candidate = candidateRepository.findById(candidateId);

        if (status == APPROVED) {
            candidate.approve();
        } else if (status == REJECTED) {
            candidate.reject();
        }

        log.info("[Candidate Status Updated] candidateId= {}, newStatus= {}", candidateId, status);
    }

    @Transactional(readOnly = true)
    public CandidateListResponse getCandidates(CandidateListReadServiceRequest request) {
        return candidateRepository.findAllByStatus(request.status(), request.page());
    }

}