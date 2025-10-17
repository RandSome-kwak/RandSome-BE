package org.kwakmunsu.randsome.admin.candidate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.candidate.controller.dto.CandidateStatusUpdateRequest;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.admin.candidate.serivce.CandidateAdminService;
import org.kwakmunsu.randsome.admin.candidate.serivce.dto.CandidateListReadServiceRequest;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@RestController
public class CandidateAdminController extends CandidateAdminDocsController {

    private final CandidateAdminService candidateAdminService;

    @Override
    @PutMapping("/matching/candidates/{candidateId}/status")
    public ResponseEntity<Void> approve(
            @PathVariable Long candidateId,
            @Valid @RequestBody CandidateStatusUpdateRequest request
    ) {
        candidateAdminService.updateCandidateStatus(candidateId, request.status());

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/matching/candidates")
    public ResponseEntity<CandidateListResponse> getCandidateApplications(
            @RequestParam CandidateStatus status,
            @RequestParam(defaultValue = "1") int page
    ) {
        CandidateListReadServiceRequest request = new CandidateListReadServiceRequest(status, page);
        CandidateListResponse response = candidateAdminService.getCandidates(request);

        return ResponseEntity.ok(response);
    }

}