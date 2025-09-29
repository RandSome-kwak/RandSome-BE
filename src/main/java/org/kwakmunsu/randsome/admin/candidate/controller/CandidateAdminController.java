package org.kwakmunsu.randsome.admin.candidate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.candidate.controller.dto.CandidateStatusUpdateRequest;
import org.kwakmunsu.randsome.admin.candidate.serivce.CandidateAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@RestController
public class CandidateAdminController {

    private final CandidateAdminService candidateAdminService;

    @PutMapping("/candidates/{candidateId}/status")
    public ResponseEntity<Void> approve(@PathVariable Long candidateId, @Valid @RequestBody CandidateStatusUpdateRequest request) {
        candidateAdminService.updateCandidateStatus(candidateId, request.status());

        return ResponseEntity.ok().build();
    }

}