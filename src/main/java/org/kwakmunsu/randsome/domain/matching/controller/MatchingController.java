package org.kwakmunsu.randsome.domain.matching.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.matching.serivce.MatchingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@RestController
public class MatchingController {

    private final MatchingService matchingService;

}