package org.kwakmunsu.randsome.admin.member.service;

import java.util.List;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.member.entity.Member;

public interface MemberAdminRepository {

    Member findById(Long id);

    List<Member> findAllByStatus(int offset, int limit, EntityStatus status);

    long countByStatus(EntityStatus active);

}