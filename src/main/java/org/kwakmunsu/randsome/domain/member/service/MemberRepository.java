package org.kwakmunsu.randsome.domain.member.service;

import org.kwakmunsu.randsome.domain.member.entity.Member;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    Member findByLoginId(String loginId);

    Member findByRefreshToken(String refreshToken);

    Member findById(Long id);

    boolean existsByInstagramId(String instagramId);

}