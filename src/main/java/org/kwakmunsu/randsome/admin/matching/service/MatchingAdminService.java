package org.kwakmunsu.randsome.admin.matching.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.admin.matching.service.dto.MatchingApplicationListServiceRequest;
import org.kwakmunsu.randsome.domain.matching.entity.Matching;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.serivce.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.matching.serivce.repository.MatchingRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MatchingAdminService {

    private final MatchingRepository matchingRepository;
    private final MatchingApplicationRepository applicationRepository;
    private final Map<MatchingType, MatchingProvider> matchingProviders;

    public MatchingAdminService(
            MatchingRepository matchingRepository,
            MatchingApplicationRepository applicationRepository,
            List<MatchingProvider> matchingProviders
    ) {
        this.matchingRepository = matchingRepository;
        this.applicationRepository = applicationRepository;
        this.matchingProviders = matchingProviders.stream()
                .collect(Collectors.toMap(
                        MatchingProvider::getType,
                        provider -> provider
                ));
    }
    /**
     * 매칭 신청 목록 조회
     */
    @Transactional(readOnly = true)
    public MatchingApplicationListResponse findApplicationsByStatus(MatchingApplicationListServiceRequest request) {
        return applicationRepository.findAllByStatus(request.status(), request.page());
    }

    /**
     * 매칭 신청 상태 변경 (승인/거절)
     */
    @Transactional
    public void updateApplicationStatus(Long applicationId, MatchingStatus status) {
        log.info("Updating application status - ID: {}, newStatus: {}", applicationId, status);

        MatchingApplication application = applicationRepository.findById(applicationId);
        switch (status) {
            case COMPLETED -> approveAndExecuteMatching(application);
            case FAILED -> application.fail();
        }
    }

    /**
     * 매칭 승인 및 실행
     */
    private void approveAndExecuteMatching(MatchingApplication application) {
        executeMatching(application);
        application.complete();
    }

    /**
     * 매칭 실행
     */
    private void executeMatching(MatchingApplication application) {
        MatchingProvider provider = matchingProviders.get(application.getMatchingType());
        List<Member> matchedCandidates = provider.match(application.getRequester(), application.getRequestedCount());

        saveMatchingResults(application, matchedCandidates);

        // TODO: notification publish
    }

    /**
     * 매칭 결과 저장
     */
    private void saveMatchingResults(MatchingApplication application, List<Member> matchedCandidates) {
        List<Matching> results = matchedCandidates.stream()
                .map(c -> Matching.create(application, c))
                .toList();
        matchingRepository.saveAll(results);
    }

}