package org.kwakmunsu.randsome.domain.matching.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.matching.controller.dto.MatchingApplicationRequest;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.service.MatchingService;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.global.annotation.AuthMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@RestController
public class MatchingController extends MatchingDocsController {

    private final MatchingService matchingService;

    @Override
    @PostMapping("/apply")
    public ResponseEntity<Long> apply(@Valid @RequestBody MatchingApplicationRequest request, @AuthMember Long memberId) {
        Long matchingApplicationId = matchingService.matchingApply(request.toServiceRequest(memberId));

        return ResponseEntity.status(HttpStatus.CREATED).body(matchingApplicationId);
    }

    @Override
    @GetMapping("/applications")
    public ResponseEntity<MatchingApplicationListResponse> getApplications(
            @AuthMember Long requesterId,
            @RequestParam MatchingStatus status
    ) {
        MatchingApplicationListResponse response = matchingService.getMatchingApplication(requesterId, status);

        return ResponseEntity.ok(response);
    }

}