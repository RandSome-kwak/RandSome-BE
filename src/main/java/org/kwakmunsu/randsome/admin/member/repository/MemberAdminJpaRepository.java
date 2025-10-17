package org.kwakmunsu.randsome.admin.member.repository;

import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAdminJpaRepository extends JpaRepository<Member, Long> {

}