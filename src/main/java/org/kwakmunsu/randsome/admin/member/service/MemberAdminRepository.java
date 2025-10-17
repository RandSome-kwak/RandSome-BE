package org.kwakmunsu.randsome.admin.member.service;

import org.kwakmunsu.randsome.admin.member.repository.dto.MemberListResponse;
import org.kwakmunsu.randsome.domain.member.entity.Member;

public interface MemberAdminRepository {

    Member findById(Long id);

    MemberListResponse findAllByPagination(int page);

    long count();

}