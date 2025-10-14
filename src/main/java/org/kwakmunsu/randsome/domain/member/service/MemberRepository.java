package org.kwakmunsu.randsome.domain.member.service;

import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.repository.dto.MemberListResponse;

public interface MemberRepository {

    Member save(Member member);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Member findByLoginId(String loginId);
    Member findByRefreshToken(String refreshToken);
    Member findById(Long id);

    // Admin 전용 메서드
    MemberListResponse findAllByPagination(int page);
    long count();

}