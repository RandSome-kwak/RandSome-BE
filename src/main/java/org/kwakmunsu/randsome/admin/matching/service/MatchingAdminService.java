package org.kwakmunsu.randsome.admin.matching.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.matching.service.dto.MatchingApplicationListServiceRequest;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.serivce.repository.MatchingApplicationRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MatchingAdminService {

    private final MatchingApplicationRepository matchingApplicationRepository;

    public MatchingApplicationListResponse getMatchingApplications(MatchingApplicationListServiceRequest request) {
        return matchingApplicationRepository.findAllByStatus(request.status(), request.page());
    }

}