package org.kwakmunsu.randsome.admin.matching.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.matching.controller.dto.MatchingApplicationUpdateRequest;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminPreviewResponse;
import org.kwakmunsu.randsome.admin.matching.service.MatchingAdminService;
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
    public ResponseEntity<PageResponse<MatchingApplicationAdminPreviewResponse>> getApplications(
            @RequestParam MatchingStatus status,
            @RequestParam(defaultValue = "1") @Min(1) int page
    ) {
        PageResponse<MatchingApplicationAdminPreviewResponse> response = matchingAdminService.getApplications(status,
                new PageRequest(page));

        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<Void> updateApplication(
            @PathVariable Long applicationId,
            @Valid @RequestBody MatchingApplicationUpdateRequest request
    ) {
        matchingAdminService.updateApplication(applicationId, request.status());

        return ResponseEntity.ok().build();
    }

}