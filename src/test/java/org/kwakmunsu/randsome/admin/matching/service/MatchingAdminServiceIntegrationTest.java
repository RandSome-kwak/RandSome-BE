package org.kwakmunsu.randsome.admin.matching.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.admin.matching.service.dto.MatchingApplicationListServiceRequest;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.repository.dto.MatchingApplicationPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.serivce.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.kwakmunsu.randsome.domain.payment.entity.Payment;
import org.kwakmunsu.randsome.domain.payment.serivce.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MatchingAdminServiceIntegrationTest {

    @Autowired
    private MatchingAdminService matchingAdminService;

    @Autowired
    private MatchingApplicationRepository matchingApplicationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private static final int APPLICATION_PER_STATUS = 25;
    private static final int TOTAL_APPLICATION_COUNT = APPLICATION_PER_STATUS * 3; // 75개
    private static final int DEFAULT_PAGE_SIZE = 20;

    @BeforeEach
    void setUp() {
        createAndSaveMemberAndCandidate();
    }

    @DisplayName("관리자가 대기 상태의 매칭 신청 목록 첫번째 페이지를 조회한다.")
    @Test
    void getPendingCandidatesFirstPage() {
        // given
        var request = new MatchingApplicationListServiceRequest(MatchingStatus.PENDING, 1);

        // when
        MatchingApplicationListResponse response = matchingAdminService.getMatchingApplications(request);

        // then
        assertThat(response.responses().size()).isEqualTo(DEFAULT_PAGE_SIZE); // 20개
        assertThat(response.hasNext()).isTrue(); // 다음 페이지 존재
        assertThat(response.totalCount()).isEqualTo(APPLICATION_PER_STATUS); // 총 25개

        // 모든 응답이 대기 상태인지 확인
        response.responses().forEach(application ->
                assertThat(application.status()).isEqualTo(MatchingStatus.PENDING.getDescription()));
    }

    @DisplayName("관리자가 대기 상태 후보자 목록 두번째 페이지를 조회한다.")
    @Test
    void getPendingCandidatesSecondPage() {
        // given
        var request = new MatchingApplicationListServiceRequest(MatchingStatus.PENDING, 2);

        // when
        MatchingApplicationListResponse response = matchingAdminService.getMatchingApplications(request);

        // then
        assertThat(response.responses().size()).isEqualTo(5); // 25 - 20 = 5개
        assertThat(response.hasNext()).isFalse(); // 마지막 페이지
        assertThat(response.totalCount()).isEqualTo(APPLICATION_PER_STATUS); // 총 25개

        // 모든 응답이 대기 상태인지 확인
        response.responses().forEach(application ->
                assertThat(application.status()).isEqualTo(MatchingStatus.PENDING.getDescription()));
    }

    @DisplayName("관리자가 완료 상태의 매칭 신청 목록 첫번째 페이지를 조회한다.")
    @Test
    void getApprovedCandidatesFirstPage() {
        // given
        var request = new MatchingApplicationListServiceRequest(MatchingStatus.COMPLETED, 1);

        // when
        MatchingApplicationListResponse response = matchingAdminService.getMatchingApplications(request);

        // then
        assertThat(response.responses().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.totalCount()).isEqualTo(APPLICATION_PER_STATUS);

        // 모든 응답이 승인 상태인지 확인
        response.responses().forEach(application ->
                assertThat(application.status()).isEqualTo(MatchingStatus.COMPLETED.getDescription()));
    }

    @DisplayName("관리자가 완료 상태의 매칭 신청 목록 두번째 페이지를 조회한다.")
    @Test
    void getApprovedCandidatesSecondPage() {
        // given
        var request = new MatchingApplicationListServiceRequest(MatchingStatus.COMPLETED, 2);

        // when
        MatchingApplicationListResponse response = matchingAdminService.getMatchingApplications(request);

        // then
        assertThat(response.responses().size()).isEqualTo(5);
        assertThat(response.hasNext()).isFalse();
        assertThat(response.totalCount()).isEqualTo(APPLICATION_PER_STATUS);

        // 모든 응답이 승인 상태인지 확인
        response.responses().forEach(application ->
                assertThat(application.status()).isEqualTo(MatchingStatus.COMPLETED.getDescription()));
    }

    @DisplayName("관리자가 실패 상태 매칭 신청 목록을 조회한다.")
    @Test
    void getRejectedCandidates() {
        // given
        var request = new MatchingApplicationListServiceRequest(MatchingStatus.FAILED, 1);

        // when
        MatchingApplicationListResponse response = matchingAdminService.getMatchingApplications(request);

        // then
        assertThat(response.responses().size()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.totalCount()).isEqualTo(APPLICATION_PER_STATUS);

        response.responses().forEach(application ->
                assertThat(application.status()).isEqualTo(MatchingStatus.FAILED.getDescription()));
    }

    @DisplayName("관리자가 거절 상태 후보자 목록 두번째 페이지를 조회한다.")
    @Test
    void getRejectedCandidatesSecondPage() {
        // given
        var request = new MatchingApplicationListServiceRequest(MatchingStatus.FAILED, 2);

        // when
        MatchingApplicationListResponse response = matchingAdminService.getMatchingApplications(request);

        // then
        assertThat(response.responses().size()).isEqualTo(5);
        assertThat(response.hasNext()).isFalse();
        assertThat(response.totalCount()).isEqualTo(APPLICATION_PER_STATUS);

        response.responses().forEach(application ->
                assertThat(application.status()).isEqualTo(MatchingStatus.FAILED.getDescription()));
    }

    @DisplayName("각 상태별로 정확히 25개씩 매칭 신청이 존재한다.")
    @Test
    void verifyStatusDistribution() {
        // given & when
        var pendingRequest = new MatchingApplicationListServiceRequest(MatchingStatus.PENDING, 1);
        var completedRequest = new MatchingApplicationListServiceRequest(MatchingStatus.COMPLETED, 1);
        var failedRequest = new MatchingApplicationListServiceRequest(MatchingStatus.FAILED, 1);

        var pendingResponse = matchingAdminService.getMatchingApplications(pendingRequest);
        var completedResponse = matchingAdminService.getMatchingApplications(completedRequest);
        var failedResponse = matchingAdminService.getMatchingApplications(failedRequest);

        // then
        assertThat(pendingResponse.totalCount()).isEqualTo(APPLICATION_PER_STATUS);
        assertThat(completedResponse.totalCount()).isEqualTo(APPLICATION_PER_STATUS);
        assertThat(failedResponse.totalCount()).isEqualTo(APPLICATION_PER_STATUS);

        // 전체 합계 확인
        long totalCount = pendingResponse.totalCount() +
                completedResponse.totalCount() +
                failedResponse.totalCount();
        assertThat(totalCount).isEqualTo(TOTAL_APPLICATION_COUNT);
    }

    @DisplayName("대기 상태 후보자의 첫 번째와 두 번째 페이지가 겹치지 않는다.")
    @Test
    void pendingCandidatesPagesDoNotOverlap() {
        // given
        var firstPageRequest = new MatchingApplicationListServiceRequest(MatchingStatus.PENDING, 1);
        var secondPageRequest = new MatchingApplicationListServiceRequest(MatchingStatus.PENDING, 2);

        // when
        var firstPageResponse = matchingAdminService.getMatchingApplications(firstPageRequest);
        var secondPageResponse = matchingAdminService.getMatchingApplications(secondPageRequest);

        // then
        var firstPageIds = firstPageResponse.responses().stream()
                .map(MatchingApplicationPreviewResponse::memberId)
                .collect(Collectors.toSet());

        var secondPageIds = secondPageResponse.responses().stream()
                .map(MatchingApplicationPreviewResponse::memberId)
                .collect(Collectors.toSet());

        // 두 페이지의 ID가 겹치지 않아야 함
        assertThat(Collections.disjoint(firstPageIds, secondPageIds)).isTrue();

        // 전체 개수 확인
        assertThat(firstPageIds.size() + secondPageIds.size())
                .isEqualTo(APPLICATION_PER_STATUS);
    }

    @DisplayName("최신순 정렬이 페이지 간에도 유지된다.")
    @Test
    void orderingMaintainedAcrossPages() {
        // given
        var firstPageRequest = new MatchingApplicationListServiceRequest(MatchingStatus.PENDING, 1);
        var secondPageRequest = new MatchingApplicationListServiceRequest(MatchingStatus.PENDING, 2);

        // when
        var firstPageResponse = matchingAdminService.getMatchingApplications(firstPageRequest);
        var secondPageResponse = matchingAdminService.getMatchingApplications(secondPageRequest);

        // then
        var firstPageApplication = firstPageResponse.responses();
        var secondPageApplication = secondPageResponse.responses();

        // 첫 번째 페이지의 마지막 항목이 두 번째 페이지의 첫 번째 항목보다 최신이어야 함
        if (!firstPageApplication.isEmpty() && !secondPageApplication.isEmpty()) {
            LocalDateTime lastOfFirstPage = firstPageApplication.getLast().appliedAt();
            LocalDateTime firstOfSecondPage = secondPageApplication.getFirst().appliedAt();

            assertThat(lastOfFirstPage).isAfterOrEqualTo(firstOfSecondPage);
        }
    }

    private void createAndSaveMemberAndCandidate() {
        // 75개를 25개씩 3그룹으로 나누어 생성
        for (int i = 0; i < TOTAL_APPLICATION_COUNT; i++) {
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
            var matchingApplication = MatchingApplication.create(member, MatchingType.RANDOM_MATCHING, new Random().nextInt(5) + 1);

            // 0-24: PENDING (기본값), 25-49: COMPLETED, 50-74: FAILED
            if (i < APPLICATION_PER_STATUS) {
                // PENDING 상태 (기본값이므로 아무것도 하지 않음)
            } else if (i < APPLICATION_PER_STATUS * 2) {
                matchingApplication.complete();
            } else {
                matchingApplication.fail();
            }

            matchingApplicationRepository.save(matchingApplication);

            // ✅ Payment 데이터 생성 추가
            Payment payment = Payment.createRandomMatching(
                    member,
                    matchingApplication.getRequestedCount()
            );
            paymentRepository.save(payment);

            // 시간 차이를 만들어 정렬 확인 가능하게 함
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}