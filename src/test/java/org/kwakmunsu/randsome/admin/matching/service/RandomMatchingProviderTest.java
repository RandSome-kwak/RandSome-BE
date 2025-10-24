package org.kwakmunsu.randsome.admin.matching.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.repository.CandidateRepository;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class RandomMatchingProviderTest {

    @Autowired
    private RandomMatchingProvider randomMatchingProvider;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        saveCandidate();
    }

    @DisplayName("랜덤 매칭으로 매칭 후보자를 가져온다.")
    @Test
    void getRandomMatching() {
        // given
        Member requester = MemberFixture.createMember();
        int count = 4;
        Gender gender = requester.getGender() == Gender.M ? Gender.F : Gender.M;

        // when
        List<Member> matched = randomMatchingProvider.match(requester, count);

        // then
        assertThat(matched).hasSize(count);
        for (Member member : matched) {
            assertThat(member.getGender()).isEqualTo(gender);
        }
    }

    private void saveCandidate() {
        for (int i = 0; i < 100; i++) {
            Gender gender;
            if (i % 2 == 0) gender = Gender.M;
            else gender = Gender.F;

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
            memberRepository.save(member);
            Candidate candidate = Candidate.create(member);
            candidate.approve();
            candidateRepository.save(candidate);
        }
    }

}