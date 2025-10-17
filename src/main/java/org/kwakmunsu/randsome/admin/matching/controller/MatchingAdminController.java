package org.kwakmunsu.randsome.admin.matching.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.matching.controller.dto.MatchingApplicationStatusUpdateRequest;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminListResponse;
import org.kwakmunsu.randsome.admin.matching.service.MatchingAdminService;
import org.kwakmunsu.randsome.admin.matching.service.dto.MatchingApplicationListServiceRequest;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin/matching")
@RequiredArgsConstructor
@RestController
public class MatchingAdminController extends MatchingAdminDocsController {

    private final MatchingAdminService matchingAdminService;

    @Override
    @GetMapping("/applications")
    public ResponseEntity<MatchingApplicationAdminListResponse> getApplications(
            @RequestParam MatchingStatus status,
            @RequestParam(defaultValue = "1") @Min(1) int page
    ) {
        MatchingApplicationListServiceRequest request = new MatchingApplicationListServiceRequest(status, page);
        MatchingApplicationAdminListResponse response = matchingAdminService.findApplicationsByStatus(request);

        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody MatchingApplicationStatusUpdateRequest request
    ) {
        matchingAdminService.updateApplicationStatus(applicationId, request.status());

        return ResponseEntity.ok().build();
    }

}