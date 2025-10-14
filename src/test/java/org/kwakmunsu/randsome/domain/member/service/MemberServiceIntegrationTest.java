package org.kwakmunsu.randsome.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberProfileUpdateServiceRequest;
import org.kwakmunsu.randsome.global.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberServiceIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("회원 정보를 업데이트한다.")
    @Test
    void updateProfile() {
        // given
        var member = MemberFixture.createMember();
        memberRepository.save(member);
        // 이상형 설명은 변경하지 않음
        var request = new MemberProfileUpdateServiceRequest(member.getId(), "newNickname", Mbti.ENFJ, "newInsta", "newIntro",
                member.getIdealDescription());

        // when
        memberService.updateProfile(request);

        entityManager.flush();
        // then
        assertThat(member).extracting(
                        Member::getNickname,
                        Member::getMbti,
                        Member::getInstagramId,
                        Member::getIntroduction,
                        Member::getIdealDescription
                )
                .containsExactly(
                        request.nickname(),
                        request.mbti(),
                        request.instagramId(),
                        request.introduction(),
                        member.getIdealDescription());
    }

    @DisplayName("회원 정보 업데이트 시 본인 닉네임을 제외하고 새로운 닉네임이 중복이면 예외를 반환한다.")
    @Test
    void failUpdateProfile() {
        // given
        var member1 = MemberFixture.createMember();
        var member2 = MemberFixture.createMember();
        memberRepository.save(member1);
        memberRepository.save(member2);

        var request = new MemberProfileUpdateServiceRequest(member1.getId(), member2.getNickname(), Mbti.ENFJ, "newInsta",
                "newIntro",
                member1.getIdealDescription());

        // when & then
        assertThatThrownBy(() -> memberService.updateProfile(request))
                .isInstanceOf(ConflictException.class);
    }

}