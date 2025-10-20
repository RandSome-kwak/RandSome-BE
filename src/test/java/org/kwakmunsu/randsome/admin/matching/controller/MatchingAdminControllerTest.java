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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.matching.controller.dto.MatchingApplicationUpdateRequest;
import org.kwakmunsu.randsome.admin.matching.repository.dto.MatchingApplicationAdminPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.global.security.annotation.TestAdmin;
import org.springframework.http.MediaType;

class MatchingAdminControllerTest extends ControllerTestSupport {

    @TestAdmin
    @DisplayName("관리자가 대기중인 매칭 신청 목록을 조회한다.")
    @Test
    void getApplications() {
        // given
        List<MatchingApplicationAdminPreviewResponse> responses = getMatchingApplicationAdminPreviewResponses();
        PageResponse<MatchingApplicationAdminPreviewResponse> response = new PageResponse<>(responses, (long) responses.size());

        given(matchingAdminService.getApplications(any(MatchingStatus.class), any(PageRequest.class)))
                .willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.PENDING.name())
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content", v -> v.assertThat().isNotNull())
                .hasPathSatisfying("$.count", v -> v.assertThat().asNumber().isEqualTo(responses.size()));

        verify(matchingAdminService).getApplications(any(MatchingStatus.class), any(PageRequest.class));
    }

    @TestAdmin
    @DisplayName("관리자가 완료된 매칭 신청 목록을 조회한다.")
    @Test
    void getCompletedApplications() {
        // given
        List<MatchingApplicationAdminPreviewResponse> responses = getMatchingApplicationAdminPreviewResponses();
        PageResponse<MatchingApplicationAdminPreviewResponse> response = new PageResponse<>(responses, (long) responses.size());
        given(matchingAdminService.getApplications(any(MatchingStatus.class), any(PageRequest.class)))
                .willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/admin/matching/applications")
                .param("status", MatchingStatus.COMPLETED.name())
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content", v -> v.assertThat().isNotNull())
                .hasPathSatisfying("$.count", v -> v.assertThat().asNumber().isEqualTo(responses.size()));
    }

    @TestAdmin
    @DisplayName("매칭 신청 상태를 업데이트한다.")
    @Test
    void updateApplication() throws JsonProcessingException {
        // given
        var request = new MatchingApplicationUpdateRequest(MatchingStatus.COMPLETED);
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        assertThat(mvcTester.put().uri("/api/v1/admin/matching/applications/{applicationId}/status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatusOk();

        // then
        verify(matchingAdminService).updateApplication(any(), any(MatchingStatus.class));
    }


    private List<MatchingApplicationAdminPreviewResponse> getMatchingApplicationAdminPreviewResponses() {
        return List.of(
                new MatchingApplicationAdminPreviewResponse(
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
                new MatchingApplicationAdminPreviewResponse(
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
        );
    }
}