package org.kwakmunsu.randsome.admin.candidate.serivce;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.admin.candidate.serivce.dto.CandidateListReadServiceRequest;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.repository.dto.CandidateListResponse;
import org.kwakmunsu.randsome.domain.candidate.repository.dto.CandidatePreviewResponse;
import org.kwakmunsu.randsome.domain.candidate.service.CandidateRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CandidateAdminServiceIntegrationTest {

    @Autowired
    private CandidateAdminService candidateAdminService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final int CANDIDATES_PER_STATUS = 25;
    private static final int TOTAL_CANDIDATE_COUNT = CANDIDATES_PER_STATUS * 3; // 75개
    private static final int DEFAULT_PAGE_SIZE = 20;

    @BeforeEach
    void setUp() {
        createAndSaveMemberAndCandidate();
    }

    @DisplayName("관리자가 대기 상태 후보자 목록 첫번째 페이지를 조회한다.")
    @Test
    void getPendingCandidatesFirstPage() {
        // given
        var request = new CandidateListReadServiceRequest(CandidateStatus.PENDING, 1);

        // when
        CandidateListResponse response = candidateAdminService.getCandidates(request);

        // then
        assertThat(response.responses().size()).isEqualTo(DEFAULT_PAGE_SIZE); // 20개
        assertThat(response.hasNext()).isTrue(); // 다음 페이지 존재
        assertThat(response.totalCount()).isEqualTo(CANDIDATES_PER_STATUS); // 총 25개

        // 모든 응답이 대기 상태인지 확인
        response.responses().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.PENDING.getDescription()));
    }

    @DisplayName("관리자가 대기 상태 후보자 목록 두번째 페이지를 조회한다.")
    @Test
    void getPendingCandidatesSecondPage() {
        // given
        var request = new CandidateListReadServiceRequest(CandidateStatus.PENDING, 2);

        // when
        CandidateListResponse response = candidateAdminService.getCandidates(request);

        // then
        assertThat(response.responses().size()).isEqualTo(5); // 25 - 20 = 5개
        assertThat(response.hasNext()).isFalse(); // 마지막 페이지
        assertThat(response.totalCount()).isEqualTo(CANDIDATES_PER_STATUS); // 총 25개

        // 모든 응답이 대기 상태인지 확인
        response.responses().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.PENDING.getDescription()));
    }

    @DisplayName("관리자가 승인 상태 후보자 목록 첫번째 페이지를 조회한다.")
    @Test
    void getApprovedCandidatesFirstPage() {
        // given
        var request = new CandidateListReadServiceRequest(CandidateStatus.APPROVED, 1);

        // when
        CandidateListResponse response = candidateAdminService.getCandidates(request);

        // then
        assertThat(response.responses().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.totalCount()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 승인 상태인지 확인
        response.responses().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.APPROVED.getDescription()));
    }

    @DisplayName("관리자가 승인 상태 후보자 목록 두번째 페이지를 조회한다.")
    @Test
    void getApprovedCandidatesSecondPage() {
        // given
        var request = new CandidateListReadServiceRequest(CandidateStatus.APPROVED, 2);

        // when
        CandidateListResponse response = candidateAdminService.getCandidates(request);

        // then
        assertThat(response.responses().size()).isEqualTo(5);
        assertThat(response.hasNext()).isFalse();
        assertThat(response.totalCount()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 승인 상태인지 확인
        response.responses().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.APPROVED.getDescription()));
    }

    @DisplayName("관리자가 거절 상태 후보자 목록을 조회한다.")
    @Test
    void getRejectedCandidates() {
        // given
        var request = new CandidateListReadServiceRequest(CandidateStatus.REJECTED, 1);

        // when
        CandidateListResponse response = candidateAdminService.getCandidates(request);

        // then
        assertThat(response.responses().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.totalCount()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 거절 상태인지 확인
        response.responses().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.REJECTED.getDescription()));
    }

    @DisplayName("관리자가 거절 상태 후보자 목록 두번째 페이지를 조회한다.")
    @Test
    void getRejectedCandidatesSecondPage() {
        // given
        var request = new CandidateListReadServiceRequest(CandidateStatus.REJECTED, 2);

        // when
        CandidateListResponse response = candidateAdminService.getCandidates(request);

        // then
        assertThat(response.responses().size()).isEqualTo(5);
        assertThat(response.hasNext()).isFalse();
        assertThat(response.totalCount()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 거절 상태인지 확인
        response.responses().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.REJECTED.getDescription()));
    }

    @DisplayName("각 상태별로 정확히 25개씩 후보자가 존재한다.")
    @Test
    void verifyStatusDistribution() {
        // given & when
        var pendingRequest = new CandidateListReadServiceRequest(CandidateStatus.PENDING, 1);
        var approvedRequest = new CandidateListReadServiceRequest(CandidateStatus.APPROVED, 1);
        var rejectedRequest = new CandidateListReadServiceRequest(CandidateStatus.REJECTED, 1);

        var pendingResponse = candidateAdminService.getCandidates(pendingRequest);
        var approvedResponse = candidateAdminService.getCandidates(approvedRequest);
        var rejectedResponse = candidateAdminService.getCandidates(rejectedRequest);

        // then
        assertThat(pendingResponse.totalCount()).isEqualTo(CANDIDATES_PER_STATUS);
        assertThat(approvedResponse.totalCount()).isEqualTo(CANDIDATES_PER_STATUS);
        assertThat(rejectedResponse.totalCount()).isEqualTo(CANDIDATES_PER_STATUS);

        // 전체 합계 확인
        long totalCount = pendingResponse.totalCount() +
                approvedResponse.totalCount() +
                rejectedResponse.totalCount();
        assertThat(totalCount).isEqualTo(TOTAL_CANDIDATE_COUNT);
    }

    @DisplayName("대기 상태 후보자의 첫 번째와 두 번째 페이지가 겹치지 않는다.")
    @Test
    void pendingCandidatesPagesDoNotOverlap() {
        // given
        var firstPageRequest = new CandidateListReadServiceRequest(CandidateStatus.PENDING, 1);
        var secondPageRequest = new CandidateListReadServiceRequest(CandidateStatus.PENDING, 2);

        // when
        var firstPageResponse = candidateAdminService.getCandidates(firstPageRequest);
        var secondPageResponse = candidateAdminService.getCandidates(secondPageRequest);

        // then
        var firstPageIds = firstPageResponse.responses().stream()
                .map(CandidatePreviewResponse::memberId)
                .collect(Collectors.toSet());

        var secondPageIds = secondPageResponse.responses().stream()
                .map(CandidatePreviewResponse::memberId)
                .collect(Collectors.toSet());

        // 두 페이지의 ID가 겹치지 않아야 함
        assertThat(Collections.disjoint(firstPageIds, secondPageIds)).isTrue();

        // 전체 개수 확인
        assertThat(firstPageIds.size() + secondPageIds.size())
                .isEqualTo(CANDIDATES_PER_STATUS);
    }

    @DisplayName("최신순 정렬이 페이지 간에도 유지된다.")
    @Test
    void orderingMaintainedAcrossPages() {
        // given
        var firstPageRequest = new CandidateListReadServiceRequest(CandidateStatus.PENDING, 1);
        var secondPageRequest = new CandidateListReadServiceRequest(CandidateStatus.PENDING, 2);

        // when
        var firstPageResponse = candidateAdminService.getCandidates(firstPageRequest);
        var secondPageResponse = candidateAdminService.getCandidates(secondPageRequest);

        // then
        var firstPageCandidates = firstPageResponse.responses();
        var secondPageCandidates = secondPageResponse.responses();

        // 첫 번째 페이지의 마지막 항목이 두 번째 페이지의 첫 번째 항목보다 최신이어야 함
        if (!firstPageCandidates.isEmpty() && !secondPageCandidates.isEmpty()) {
            LocalDateTime lastOfFirstPage = firstPageCandidates.get(firstPageCandidates.size() - 1).appliedAt();
            LocalDateTime firstOfSecondPage = secondPageCandidates.get(0).appliedAt();

            assertThat(lastOfFirstPage).isAfterOrEqualTo(firstOfSecondPage);
        }
    }

    private void createAndSaveMemberAndCandidate() {
        // 75개를 25개씩 3그룹으로 나누어 생성
        for (int i = 0; i < TOTAL_CANDIDATE_COUNT; i++) {
            Member member = Member.createMember(
                    "loginId" + i,
                    "password" + i,
                    "legalName" + i,
                    "nickname" + i,
                    Gender.M,
                    Mbti.ENFJ,
                    "instagramId" + i,
                    "introduction" + i,
                    "idealDescription" + i
            );
            memberRepository.save(member);

            Candidate candidate = Candidate.create(member);

            // 0-24: PENDING (기본값), 25-49: APPROVED, 50-74: REJECTED
            if (i < CANDIDATES_PER_STATUS) {
                // PENDING 상태 (기본값이므로 아무것도 하지 않음)
            } else if (i < CANDIDATES_PER_STATUS * 2) {
                candidate.approve();
            } else {
                candidate.reject();
            }

            candidateRepository.save(candidate);

            // 시간 차이를 만들어 정렬 확인 가능하게 함
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}