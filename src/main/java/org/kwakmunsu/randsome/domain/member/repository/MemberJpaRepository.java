package org.kwakmunsu.randsome.domain.member.repository;

import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);

}