package org.kwakmunsu.randsome.domain.candidate.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.candidate.service.CandidateService;
import org.kwakmunsu.randsome.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/matching/candidates")
@RequiredArgsConstructor
@RestController
public class CandidateController extends CandidateDocsController{

    private final CandidateService candidateService;

    @Override
    @PostMapping
    public ResponseEntity<Void> register(@AuthMember Long memberId) {
        candidateService.register(memberId);

        return ResponseEntity.ok().build();
    }

}