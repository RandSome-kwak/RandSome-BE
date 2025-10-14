package org.kwakmunsu.randsome.admin.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.statistics.service.StatisticsAdminService;
import org.kwakmunsu.randsome.admin.statistics.service.dto.MatchingStatisticsAdminResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StatisticsAdminController extends StatisticsAdminDocsController{

    private final StatisticsAdminService statisticsAdminService;

    @Override
    @GetMapping("/api/v1/admin/statistics")
    public ResponseEntity<MatchingStatisticsAdminResponse> getStatistics() {
        MatchingStatisticsAdminResponse response = statisticsAdminService.getMatchingStatistics();

        return ResponseEntity.ok(response);
    }

}