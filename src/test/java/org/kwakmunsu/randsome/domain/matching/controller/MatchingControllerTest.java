package org.kwakmunsu.randsome.domain.matching.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.ControllerTestSupport;
import org.kwakmunsu.randsome.domain.matching.controller.dto.MatchingApplicationRequest;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingEventType;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationListResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingApplicationPreviewResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingEventResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingMemberResponse;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingReadResponse;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.global.security.annotation.TestMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MatchingControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("매칭 신청을 한다.")
    @Test
    void apply() throws JsonProcessingException {
        // given
        given(matchingService.applyMatching(any())).willReturn(1L);
        var request = new MatchingApplicationRequest(MatchingType.RANDOM_MATCHING, 3);
        String requestJson = objectMapper.writeValueAsString(request);

        // when & then
        assertThat(mvcTester.post().uri("/api/v1/matching/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatus(HttpStatus.CREATED)
                .bodyText()
                .isEqualTo("1");
    }

    @TestMember
    @DisplayName("승인 대기 또는 취소된 매칭 신청 목록을 조회한다.")
    @Test
    void getApplications() {
        // given
        var status = MatchingStatus.PENDING;
        var response = new MatchingApplicationListResponse(
                List.of(
                        new MatchingApplicationPreviewResponse(
                                1L,
                                3,
                                MatchingStatus.PENDING.getDescription(),
                                MatchingType.RANDOM_MATCHING.getDescription(),
                                LocalDateTime.now()
                        ),
                        new MatchingApplicationPreviewResponse(
                                2L,
                                5,
                                MatchingStatus.FAILED.getDescription(),
                                MatchingType.IDEAL_MATCHING.getDescription(),
                                LocalDateTime.now().minusDays(1)
                        )
                )
        );
        given(matchingService.getMatchingApplication(any(Long.class), any(MatchingStatus.class)))
                .willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/matching/applications")
                .param("status", status.name())
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .hasPathSatisfying("$.responses[0].applicationId", v -> v.assertThat().asNumber().isEqualTo(1))
                .hasPathSatisfying("$.responses[0].matchingType", v -> v.assertThat().isEqualTo("랜덤"))
                .hasPathSatisfying("$.responses[0].requestedCount", v -> v.assertThat().isEqualTo(3))
                .hasPathSatisfying("$.responses[0].matchingStatus", v -> v.assertThat().isEqualTo("대기"))
                .hasPathSatisfying("$.responses[1].applicationId", v -> v.assertThat().asNumber().isEqualTo(2))
                .hasPathSatisfying("$.responses[1].matchingType", v -> v.assertThat().isEqualTo("이상형"))
                .hasPathSatisfying("$.responses[1].requestedCount", v -> v.assertThat().isEqualTo(5))
                .hasPathSatisfying("$.responses[1].matchingStatus", v -> v.assertThat().isEqualTo("실패"));
    }

    @TestMember
    @DisplayName("승인 완료된 매칭 신청 목록을 조회한다.")
    @Test
    void getApplicationsByStatus() {
        // given
        var status = MatchingStatus.COMPLETED;
        var response = new MatchingApplicationListResponse(
                List.of(
                        new MatchingApplicationPreviewResponse(
                                1L,
                                3,
                                MatchingStatus.COMPLETED.getDescription(),
                                MatchingType.RANDOM_MATCHING.getDescription(),
                                LocalDateTime.now()
                        ),
                        new MatchingApplicationPreviewResponse(
                                2L,
                                5,
                                MatchingStatus.COMPLETED.getDescription(),
                                MatchingType.IDEAL_MATCHING.getDescription(),
                                LocalDateTime.now().minusDays(1)
                        )
                )
        );
        given(matchingService.getMatchingApplication(any(Long.class), any(MatchingStatus.class)))
                .willReturn(response);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/matching/applications")
                .param("status", status.name())
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .hasPathSatisfying("$.responses[0].applicationId", v -> v.assertThat().asNumber().isEqualTo(1))
                .hasPathSatisfying("$.responses[0].matchingType", v -> v.assertThat().isEqualTo("랜덤"))
                .hasPathSatisfying("$.responses[0].requestedCount", v -> v.assertThat().isEqualTo(3))
                .hasPathSatisfying("$.responses[0].matchingStatus", v -> v.assertThat().isEqualTo("완료"))
                .hasPathSatisfying("$.responses[1].applicationId", v -> v.assertThat().asNumber().isEqualTo(2))
                .hasPathSatisfying("$.responses[1].matchingType", v -> v.assertThat().isEqualTo("이상형"))
                .hasPathSatisfying("$.responses[1].requestedCount", v -> v.assertThat().isEqualTo(5))
                .hasPathSatisfying("$.responses[1].matchingStatus", v -> v.assertThat().isEqualTo("완료"));
    }

    @TestMember
    @DisplayName("승인 처리 된 자신의 매칭 신청 결과를 조회한다.")
    @Test
    void get() {
        // given
        var response = MatchingReadResponse.builder()
                .matchingType("랜덤")
                .requestedCount(3)
                .requestedAt(LocalDateTime.now())
                .memberResponse(List.of(
                        new MatchingMemberResponse("nickname1", Mbti.ENFJ, "instargramId1", "introdution1"),
                        new MatchingMemberResponse("nickname2", Mbti.ENFJ, "instargramId2", "introdution2"),
                        new MatchingMemberResponse("nickname3", Mbti.ENFJ, "instargramId3", "introdution3")
                ))
                .build();
        given(matchingService.getMatching(any(Long.class), any(Long.class))).willReturn(response);

        MatchingMemberResponse first = response.memberResponse().getFirst();

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/matching/{applicationId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.matchingType", v -> v.assertThat().isEqualTo("랜덤"))
                .hasPathSatisfying("$.requestedCount", v -> v.assertThat().isEqualTo(response.requestedCount()))
                .hasPathSatisfying("$.requestedAt", v -> v.assertThat().isEqualTo((response.requestedAt().toString())))
                .hasPathSatisfying("$.memberResponse[0].nickname", v -> v.assertThat().isEqualTo(first.nickname()))
                .hasPathSatisfying("$.memberResponse[0].mbti", v -> v.assertThat().isEqualTo(first.mbti().name()))
                .hasPathSatisfying("$.memberResponse[0].instagramId", v -> v.assertThat().isEqualTo(first.instagramId()))
                .hasPathSatisfying("$.memberResponse[0].introduction", v -> v.assertThat().isEqualTo(first.introduction()));
    }

    @TestMember
    @DisplayName("최근 매칭 소식을 조회한다.")
    @Test
    void getRecentNews() {
        // given
        var eventResponses = List.of(
                MatchingEventResponse.from(MatchingEventType.MATCHING, "매칭 신청", LocalDateTime.now()),
                MatchingEventResponse.from(MatchingEventType.CANDIDATE, "매칭 후보자 신청", LocalDateTime.now()),
                MatchingEventResponse.from(MatchingEventType.MATCHING, "매칭 신청", LocalDateTime.now())
        );

        given(matchingService.getRecentMatchingNews(any(Integer.class))).willReturn(eventResponses);

        // when & then
        assertThat(mvcTester.get().uri("/api/v1/matching/recent-news")
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.length()", v -> v.assertThat().isEqualTo(eventResponses.size()))
                .hasPathSatisfying("$[0].eventType", v -> v.assertThat().isEqualTo(MatchingEventType.MATCHING.getDescription()))
                .hasPathSatisfying("$[0].eventDescription", v -> v.assertThat().isEqualTo("매칭 신청"))
                .hasPathSatisfying("$[0].createdAt", v -> v.assertThat().isNotNull())
                .hasPathSatisfying("$[1].eventType", v -> v.assertThat().isEqualTo(MatchingEventType.CANDIDATE.getDescription()))
                .hasPathSatisfying("$[1].eventDescription", v -> v.assertThat().isEqualTo("매칭 후보자 신청"))
                .hasPathSatisfying("$[1].createdAt", v -> v.assertThat().isNotNull())
                .hasPathSatisfying("$[2].eventType", v -> v.assertThat().isEqualTo(MatchingEventType.MATCHING.getDescription()))
                .hasPathSatisfying("$[2].eventDescription", v -> v.assertThat().isEqualTo("매칭 신청"))
                .hasPathSatisfying("$[2].createdAt", v -> v.assertThat().isNotNull());
    }

}