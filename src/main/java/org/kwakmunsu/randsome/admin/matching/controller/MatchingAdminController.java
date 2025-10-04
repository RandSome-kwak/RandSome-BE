package org.kwakmunsu.randsome.admin.matching.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.matching.service.MatchingAdminService;
import org.kwakmunsu.randsome.admin.matching.service.dto.MatchingApplicationListServiceRequest;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MatchingAdminController extends MatchingAdminDocsController {

    private final MatchingAdminService matchingAdminService;

    @Override
    @GetMapping("/api/v1/admin/matching/applications")
    public ResponseEntity<MatchingApplicationListResponse> getApplications(
            @RequestParam MatchingStatus status,
            @RequestParam(defaultValue = "1") @Min(1) int page
    ) {
        MatchingApplicationListServiceRequest request = new MatchingApplicationListServiceRequest(status, page);
        MatchingApplicationListResponse response = matchingAdminService.getMatchingApplications(request);

        return ResponseEntity.ok(response);
    }

}