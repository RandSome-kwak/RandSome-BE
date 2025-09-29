package org.kwakmunsu.randsome.domain.candidate.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.candidate.serivce.CandidateService;
import org.kwakmunsu.randsome.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/matching/candidates")
@RequiredArgsConstructor
@RestController
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<Void> register(@AuthMember Long memberId) {
        candidateService.register(memberId);

        return ResponseEntity.ok().build();
    }

}