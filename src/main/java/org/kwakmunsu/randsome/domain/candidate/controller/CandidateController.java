package org.kwakmunsu.randsome.domain.candidate.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.candidate.serivce.CandidateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
@RestController
public class CandidateController {

    private final CandidateService candidateService;

}