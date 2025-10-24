package org.kwakmunsu.randsome.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    public Member findByLoginId(String loginId) {
        return memberJpaRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_LOGIN_ID));
    }

    public boolean existsByLoginId(String loginId) {
        return memberJpaRepository.existsByLoginId(loginId);
    }

    public boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }

    public Member findByRefreshToken(String refreshToken) {
        return memberJpaRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_TOKEN));
    }

    public Member findById(Long id) {
        return memberJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER));
    }

    public boolean existsByInstagramId(String instagramId) {
        return memberJpaRepository.existsByInstagramId(instagramId);
    }

}