package org.kwakmunsu.randsome.domain.matching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.matching.entity.Matching;
import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.matching.service.dto.MatchingReadResponse;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingApplicationRepository;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingRepository;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.global.exception.ForbiddenException;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
record MatchingQueryServiceIntegrationTest(
        MatchingQueryService matchingQueryService,
        MemberRepository memberRepository,
        MatchingApplicationRepository matchingApplicationRepository,
        MatchingRepository matchingRepository
) {

    @DisplayName("자신의 매칭 결과를 조회한다.")
    @Test
    void getMatching() {
        // given
        var requester = MemberFixture.createMember();
        int requestedCount = 3;
        memberRepository.save(requester);
        List<Member> selectedMembers = saveSelectedMember(requestedCount);

        var application = MatchingApplication.create(requester, MatchingType.RANDOM_MATCHING, requestedCount);
        application.complete();
        matchingApplicationRepository.save(application);

        List<Matching> matchings = selectedMembers.stream()
                .map(selectedMember -> Matching.create(application, selectedMember))
                .toList();
        matchingRepository.saveAll(matchings);

        // when
        var matchingReadResponse = matchingQueryService.getMatching(requester.getId(), application.getId());

        // then
        assertThat(matchingReadResponse).extracting(
                        MatchingReadResponse::matchingType,
                        MatchingReadResponse::requestedCount,
                        MatchingReadResponse::requestedAt
                )
                .containsExactly(
                        application.getMatchingType().getDescription(),
                        application.getRequestedCount(),
                        application.getCreatedAt()
                );

        assertThat(matchingReadResponse.memberResponse()).hasSize(requestedCount);
    }

    @DisplayName("승인이 나지 않은 매칭 신청의 매칭 결과를 조회하면 예외가 발생한다.")
    @Test
    void failGetMatching() {
        // given
        var requester = MemberFixture.createMember();
        int requestedCount = 3;
        memberRepository.save(requester);

        var application = MatchingApplication.create(requester, MatchingType.RANDOM_MATCHING, requestedCount);
        matchingApplicationRepository.save(application);

        // when & then
        assertThatThrownBy(() -> matchingQueryService.getMatching(requester.getId(), application.getId()))
            .isInstanceOf(ForbiddenException.class);
    }

    private List<Member> saveSelectedMember(int requestedCount) {

        List<Member> savedMembers = new ArrayList<>();
        for (int i = 0; i < requestedCount; i++) {
            Gender gender;
            if (i % 2 == 0) {
                gender = Gender.M;
            } else {
                gender = Gender.F;
            }

            Member member = Member.createMember(
                    "loginId" + i,
                    "password" + i,
                    "legalName" + i,
                    "nickname" + i,
                    gender,
                    Mbti.ENFJ,
                    "instagramId" + i,
                    "introduction" + i,
                    "idealDescription" + i
            );
            savedMembers.add(memberRepository.save(member));
        }
        return savedMembers;
    }

}