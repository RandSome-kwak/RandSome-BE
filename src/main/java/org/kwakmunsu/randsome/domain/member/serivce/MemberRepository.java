package org.kwakmunsu.randsome.domain.member.serivce;

import org.kwakmunsu.randsome.admin.member.service.dto.MemberListResponse;
import org.kwakmunsu.randsome.domain.member.entity.Member;

public interface MemberRepository {

    Member save(Member member);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Member findByLoginId(String loginId);
    Member findByRefreshToken(String refreshToken);
    Member findById(Long id);
    MemberListResponse findAll(int page);

}