package org.kwakmunsu.randsome.domain.member.repository;

import java.util.Optional;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByRefreshToken(String refreshToken);

}