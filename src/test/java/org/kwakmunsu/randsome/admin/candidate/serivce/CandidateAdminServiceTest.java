package org.kwakmunsu.randsome.admin.candidate.serivce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.randsome.domain.candidate.entity.Candidate;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.MemberFixture;
import org.kwakmunsu.randsome.domain.member.enums.Role;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CandidateAdminServiceTest {

    @Mock
    private CandidateAdminRepository candidateAdminRepository;

    @InjectMocks
    private CandidateAdminService candidateAdminService;

    @DisplayName("관리자가 후보자 요청을 승인한다.")
    @Test
    void approve() {
        // given
        var member = MemberFixture.createMember();
        var candidate = Candidate.create(member);

        given(candidateAdminRepository.findByIdWithMemberAndStatus(any(Long.class))).willReturn(candidate);

        assertThat(candidate.getCandidateStatus()).isEqualTo(CandidateStatus.PENDING);

        // when
        candidateAdminService.updateCandidateStatus(1L, CandidateStatus.APPROVED);

        // then
        assertThat(candidate.getCandidateStatus()).isEqualTo(CandidateStatus.APPROVED);
        assertThat(candidate.getMember().getRole()).isEqualTo(Role.ROLE_CANDIDATE);
    }

    @DisplayName("관리자가 후보자 요청을 거절한다.")
    @Test
    void reject() {
        // given
        var member = MemberFixture.createMember();
        var candidate = Candidate.create(member);

        given(candidateAdminRepository.findByIdWithMemberAndStatus(any(Long.class))).willReturn(candidate);

        assertThat(candidate.getCandidateStatus()).isEqualTo(CandidateStatus.PENDING);

        // when
        candidateAdminService.updateCandidateStatus(1L, CandidateStatus.REJECTED);

        // then
        assertThat(candidate.getCandidateStatus()).isEqualTo(CandidateStatus.REJECTED);
        assertThat(candidate.getMember().getRole()).isEqualTo(Role.ROLE_MEMBER);
    }

    @DisplayName("후보자 요청이 존재하지 않을 경우 예외를 반환한다.")
    @Test
    void failUpdateStatus() {
        // given
        given(candidateAdminRepository.findByIdWithMemberAndStatus(any(Long.class))).willThrow(
                new NotFoundException(ErrorStatus.NOT_FOUND_CANDIDATE));

        // when & then
        assertThatThrownBy(() -> candidateAdminService.updateCandidateStatus(1L, CandidateStatus.APPROVED))
                .isInstanceOf(NotFoundException.class);

    }

}