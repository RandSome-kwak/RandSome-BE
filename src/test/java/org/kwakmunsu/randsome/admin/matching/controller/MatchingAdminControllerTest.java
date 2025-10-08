package org.kwakmunsu.randsome.admin.matching.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.admin.matching.controller.dto.MatchingApplicationStatusUpdateRequest;
import org.kwakmunsu.randsome.admin.matching.service.dto.MatchingApplicationListServiceRequest;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.repository.dto.AdminMatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.repository.dto.AdminMatchingApplicationPreviewResponse;
import org.kwakmunsu.randsome.global.security.annotation.TestAdmin;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MatchingAdminControllerTest extends ControllerTestSupport {

    private AdminMatchingApplicationListResponse adminMatchingApplicationListResponse;

    @BeforeEach
    void setUp() {
        adminMatchingApplicationListResponse = AdminMatchingApplicationListResponse.builder()
                .responses(List.of(
                        new AdminMatchingApplicationPreviewResponse(
                                1L,
                                1L,
                                "김철수",
                                "철수닉네임",
                                "남자",
                                "랜덤 매칭",
                                2,
                                BigDecimal.valueOf(1000),
                                LocalDateTime.of(2025, 10, 4, 14, 30),
                                "대기중"
                        ),
                        new AdminMatchingApplicationPreviewResponse(
                                2L,
                                2L,
                                "이영희",
                                "영희닉네임",
                                "여자",
                                "이상형 매칭",
                                3,
                                BigDecimal.valueOf(1500),
                                LocalDateTime.of(2025, 10, 4, 15, 0),
                                "대기중"
                        )
                ))
                .hasNext(true)
                .totalCount(25L)
                .build();
    }

    @TestAdmin
    @DisplayName("관리자가 대기중인 매칭 신청 목록을 조회한다.")
    @Test
    void getApplications() {
        // given
        given(matchingAdminService.findApplicationsByStatus(any(MatchingApplicationListServiceRequest.class)))
                .willReturn(adminMatchingApplicationListResponse);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.PENDING.name())
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.responses", v -> v.assertThat().isNotNull())
                .hasPathSatisfying("$.hasNext", v -> v.assertThat().isEqualTo(true))
                .hasPathSatisfying("$.totalCount", v -> v.assertThat().asNumber().isEqualTo(25))
                .hasPathSatisfying("$.responses[0].applicationId", v -> v.assertThat().asNumber().isEqualTo(1))
                .hasPathSatisfying("$.responses[0].memberId", v -> v.assertThat().asNumber().isEqualTo(1))
                .hasPathSatisfying("$.responses[0].legalName", v -> v.assertThat().isEqualTo("김철수"))
                .hasPathSatisfying("$.responses[1].memberId", v -> v.assertThat().asNumber().isEqualTo(2));

        verify(matchingAdminService).findApplicationsByStatus(any(MatchingApplicationListServiceRequest.class));
    }

    @TestAdmin
    @DisplayName("관리자가 완료된 매칭 신청 목록을 조회한다.")
    @Test
    void getCompletedApplications() {
        // given
        given(matchingAdminService.findApplicationsByStatus(any(MatchingApplicationListServiceRequest.class)))
                .willReturn(adminMatchingApplicationListResponse);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.COMPLETED.name())
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.responses", v -> v.assertThat().isNotNull())
                .hasPathSatisfying("$.hasNext", v -> v.assertThat().isEqualTo(true))
                .hasPathSatisfying("$.totalCount", v -> v.assertThat().asNumber().isEqualTo(25));
    }

    @TestAdmin
    @DisplayName("관리자가 page 기본값으로 매칭 신청 목록을 조회한다.")
    @Test
    void getApplicationsWithDefaultPage() {
        // given
        given(matchingAdminService.findApplicationsByStatus(any(MatchingApplicationListServiceRequest.class)))
                .willReturn(adminMatchingApplicationListResponse);

        ArgumentCaptor<MatchingApplicationListServiceRequest> captor =
                ArgumentCaptor.forClass(MatchingApplicationListServiceRequest.class);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.PENDING.name())
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk();

        verify(matchingAdminService).findApplicationsByStatus(captor.capture());

        MatchingApplicationListServiceRequest capturedRequest = captor.getValue();
        assertThat(capturedRequest.status()).isEqualTo(MatchingStatus.PENDING);
        assertThat(capturedRequest.page()).isEqualTo(1); // 기본값 확인
    }

    @TestAdmin
    @DisplayName("관리자가 2페이지의 매칭 신청 목록을 조회한다.")
    @Test
    void getApplicationsSecondPage() {
        // given
        AdminMatchingApplicationListResponse secondPageResponse = AdminMatchingApplicationListResponse.builder()
                .responses(List.of())
                .hasNext(false)
                .totalCount(25L)
                .build();

        given(matchingAdminService.findApplicationsByStatus(any(MatchingApplicationListServiceRequest.class)))
                .willReturn(secondPageResponse);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.PENDING.name())
                .param("page", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.hasNext", v -> v.assertThat().isEqualTo(false));
    }

    @TestAdmin
    @DisplayName("빈 결과를 반환하는 경우")
    @Test
    void getApplicationsEmptyResult() {
        // given
        AdminMatchingApplicationListResponse emptyResponse = AdminMatchingApplicationListResponse.builder()
                .responses(List.of())
                .hasNext(false)
                .totalCount(0L)
                .build();

        given(matchingAdminService.findApplicationsByStatus(any(MatchingApplicationListServiceRequest.class)))
                .willReturn(emptyResponse);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.PENDING.name())
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.responses", v -> v.assertThat().asList().isEmpty())
                .hasPathSatisfying("$.totalCount", v -> v.assertThat().asNumber().isEqualTo(0))
                .hasPathSatisfying("$.hasNext", v -> v.assertThat().isEqualTo(false));
    }

    @TestAdmin
    @DisplayName("status 파라미터가 누락된 경우 실패한다.")
    @Test
    void failGetApplicationsMissingStatus() {
        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @TestAdmin
    @DisplayName("잘못된 status 값으로 요청하면 실패한다.")
    @Test
    void failGetApplicationsInvalidStatus() {
        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", "INVALID_STATUS")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @TestAdmin
    @DisplayName("잘못된 page 값으로 요청하면 실패한다.")
    @Test
    void failGetApplicationsInvalidPage() {
        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.PENDING.name())
                .param("page", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @TestAdmin
    @DisplayName("page 파라미터가 문자열인 경우 실패한다.")
    @Test
    void failGetApplicationsNonNumericPage() {
        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.PENDING.name())
                .param("page", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @TestAdmin
    @DisplayName("매칭 신청 상태를 업데이트한다.")
    @Test
    void updateApplicationStatus() throws JsonProcessingException {
        // given
        var request = new MatchingApplicationStatusUpdateRequest(MatchingStatus.COMPLETED);
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        assertThat(mvcTester.put().uri("/api/v1/admin/matching/applications/{applicationId}/status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatusOk();

        // then
        verify(matchingAdminService).updateApplicationStatus(any(), any(MatchingStatus.class));
    }

}