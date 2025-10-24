package org.kwakmunsu.randsome.admin.candidate.serivce;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.candidate.repository.dto.CandidatePreviewResponse;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.candidate.repository.CandidateRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.repository.MemberRepository;
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
    private static final int DEFAULT_PAGE_SIZE = 10;

    @BeforeEach
    void setUp() {
        createAndSaveMemberAndCandidate();
    }

    @DisplayName("관리자가 대기 상태 후보자 목록 첫번째 페이지를 조회한다.")
    @Test
    void getPendingCandidatesFirstPage() {
        // given
        var pageRequest = new PageRequest(1);

        // when
        PageResponse<CandidatePreviewResponse> response = candidateAdminService.getCandidates(CandidateStatus.PENDING, pageRequest);

        // then
        assertThat(response.content().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(response.count()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 대기 상태인지 확인
        response.content().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.PENDING.getDescription()));
    }

    @DisplayName("관리자가 대기 상태 후보자 목록 세번째 페이지를 조회한다.")
    @Test
    void getPendingCandidatesSecondPage() {
        // given
        var pageRequest = new PageRequest(3);

        // when
        PageResponse<CandidatePreviewResponse> response = candidateAdminService.getCandidates(CandidateStatus.PENDING, pageRequest);

        // then
        assertThat(response.content().size()).isEqualTo(5);
        assertThat(response.count()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 대기 상태인지 확인
        response.content().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.PENDING.getDescription()));
    }

    @DisplayName("관리자가 승인 상태 후보자 목록 첫번째 페이지를 조회한다.")
    @Test
    void getApprovedCandidatesFirstPage() {
        // given
        var pageRequest = new PageRequest(1);

        // when
        PageResponse<CandidatePreviewResponse> response = candidateAdminService.getCandidates(CandidateStatus.APPROVED, pageRequest);

        // then
        assertThat(response.content().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(response.count()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 승인 상태인지 확인
        response.content().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.APPROVED.getDescription()));
    }

    @DisplayName("관리자가 승인 상태 후보자 목록 두번째 페이지를 조회한다.")
    @Test
    void getApprovedCandidatesSecondPage() {
        // given
        var pageRequest = new PageRequest(3);

        // when
        PageResponse<CandidatePreviewResponse> response = candidateAdminService.getCandidates(CandidateStatus.APPROVED, pageRequest);

        // then
        assertThat(response.content().size()).isEqualTo(5);
        assertThat(response.count()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 승인 상태인지 확인
        response.content().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.APPROVED.getDescription()));
    }

    @DisplayName("관리자가 거절 상태 후보자 목록을 조회한다.")
    @Test
    void getRejectedCandidates() {
        // given
        var pageRequest = new PageRequest(1);

        // when
        PageResponse<CandidatePreviewResponse> response = candidateAdminService.getCandidates(CandidateStatus.REJECTED, pageRequest);

        // then
        assertThat(response.content().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(response.count()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 거절 상태인지 확인
        response.content().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.REJECTED.getDescription()));
    }

    @DisplayName("관리자가 거절 상태 후보자 목록 세번째 페이지를 조회한다.")
    @Test
    void getRejectedCandidatesSecondPage() {
        // given
        var pageRequest = new PageRequest(3);

        // when
        PageResponse<CandidatePreviewResponse> response = candidateAdminService.getCandidates(CandidateStatus.REJECTED, pageRequest);

        // then
        assertThat(response.content().size()).isEqualTo(5);
        assertThat(response.count()).isEqualTo(CANDIDATES_PER_STATUS);

        // 모든 응답이 거절 상태인지 확인
        response.content().forEach(candidate ->
                assertThat(candidate.status()).isEqualTo(CandidateStatus.REJECTED.getDescription()));
    }

    @DisplayName("각 상태별로 정확히 25개씩 후보자가 존재한다.")
    @Test
    void verifyStatusDistribution() {
        // given & when
        var pageRequest = new PageRequest(1);

        var pendingResponse = candidateAdminService.getCandidates(CandidateStatus.PENDING, pageRequest);
        var approvedResponse = candidateAdminService.getCandidates(CandidateStatus.APPROVED, pageRequest);
        var rejectedResponse = candidateAdminService.getCandidates(CandidateStatus.REJECTED, pageRequest);

        // then
        assertThat(pendingResponse.count()).isEqualTo(CANDIDATES_PER_STATUS);
        assertThat(approvedResponse.count()).isEqualTo(CANDIDATES_PER_STATUS);
        assertThat(rejectedResponse.count()).isEqualTo(CANDIDATES_PER_STATUS);

        // 전체 합계 확인
        long totalCount = pendingResponse.count() +
                approvedResponse.count() +
                rejectedResponse.count();
        assertThat(totalCount).isEqualTo(TOTAL_CANDIDATE_COUNT);
    }

    @DisplayName("대기 상태 후보자의 첫 번째와 두 번째 페이지가 겹치지 않는다.")
    @Test
    void pendingCandidatesPagesDoNotOverlap() {
        // when
        var firstPageResponse = candidateAdminService.getCandidates(CandidateStatus.PENDING, new PageRequest(1));
        var secondPageResponse = candidateAdminService.getCandidates(CandidateStatus.PENDING, new PageRequest(2));

        // then
        var firstPageIds = firstPageResponse.content().stream()
                .map(CandidatePreviewResponse::memberId)
                .collect(Collectors.toSet());

        var secondPageIds = secondPageResponse.content().stream()
                .map(CandidatePreviewResponse::memberId)
                .collect(Collectors.toSet());

        // 두 페이지의 ID가 겹치지 않아야 함
        assertThat(Collections.disjoint(firstPageIds, secondPageIds)).isTrue();

        // 전체 개수 확인
        assertThat(firstPageIds.size() + secondPageIds.size()).isEqualTo(20); // 첫 번째 페이지 10개 + 두 번째 페이지 10개 = 20개
    }

    @DisplayName("최신순 정렬이 페이지 간에도 유지된다.")
    @Test
    void orderingMaintainedAcrossPages() {

        // when
        var firstPageResponse = candidateAdminService.getCandidates(CandidateStatus.PENDING, new PageRequest(1));
        var secondPageResponse = candidateAdminService.getCandidates(CandidateStatus.PENDING, new PageRequest(2));

        // then
        var firstPageCandidates = firstPageResponse.content();
        var secondPageCandidates = secondPageResponse.content();

        // 첫 번째 페이지의 마지막 항목이 두 번째 페이지의 첫 번째 항목보다 최신이어야 함
        if (!firstPageCandidates.isEmpty() && !secondPageCandidates.isEmpty()) {
            LocalDateTime lastOfFirstPage = firstPageCandidates.getLast().appliedAt();
            LocalDateTime firstOfSecondPage = secondPageCandidates.getFirst().appliedAt();

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