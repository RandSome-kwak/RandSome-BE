package org.kwakmunsu.randsome.domain.candidate.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.service.MemberRepository;
import org.kwakmunsu.randsome.global.exception.ConflictException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private CandidateService candidateService;

    @DisplayName("후보자 등록에 성공한다.")
    @Test
    void register() {
        // given
        var member = MemberFixture.createMember(1L);

        given(candidateRepository.findByMemberId(any(Long.class))).willReturn(Optional.empty());
        given(memberRepository.findById(any(Long.class))).willReturn(member);

        // when
        candidateService.register(member.getId());

        // then
        verify(candidateRepository).save(any(Candidate.class));
    }

    @DisplayName("이미 승인된 회원은 후보자 등록에 실패한다.")
    @Test
    void failRegisterWhenApprove() {
        // given
        var member = MemberFixture.createMember(1L);
        var candidate = Candidate.create(member);
        candidate.approve();

        given(candidateRepository.findByMemberId(any(Long.class))).willReturn(Optional.of(candidate));

        // when & then
        assertThatThrownBy(() -> candidateService.register(member.getId()))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ErrorStatus.ALREADY_APPROVED.getMessage());
    }

    @DisplayName("승인 대기 중인 회원은 후보자 등록에 실패한다.")
    @Test
    void failRegisterWhenPending() {
        // given
        var member = MemberFixture.createMember(1L);
        var candidate = Candidate.create(member);

        given(candidateRepository.findByMemberId(any(Long.class))).willReturn(Optional.of(candidate));

        // when & then
        assertThatThrownBy(() -> candidateService.register(member.getId()))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ErrorStatus.PENDING_MEMBER.getMessage());
    }

    @DisplayName("승인 거절된 회원이라면 후보자 등록에 성공한다.")
    @Test
    void registerSuccessWhenReject() {
        // given
        var member = MemberFixture.createMember(1L);
        var candidate = Candidate.create(member);
        candidate.reject();

        given(candidateRepository.findByMemberId(any(Long.class))).willReturn(Optional.of(candidate));
        given(memberRepository.findById(any(Long.class))).willReturn(member);

        // when
        candidateService.register(member.getId());

        // then
        verify(candidateRepository).save(any(Candidate.class));
    }

}