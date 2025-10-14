package org.kwakmunsu.randsome.domain.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.statistics.service.StatisticsService;
import org.kwakmunsu.randsome.domain.statistics.service.dto.MatchingStatisticsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StatisticsController extends StatisticsDocsController {

    private final StatisticsService statisticsService;

    @Override
    @GetMapping("/api/v1/matching/statistics")
    public ResponseEntity<MatchingStatisticsResponse> getStatistics() {
        MatchingStatisticsResponse response = statisticsService.getMatchingStatistics();

        return ResponseEntity.ok(response);
    }

}