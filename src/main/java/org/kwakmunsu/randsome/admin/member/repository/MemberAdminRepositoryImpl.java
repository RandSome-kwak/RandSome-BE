package org.kwakmunsu.randsome.admin.member.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.member.repository.dto.MemberListResponse;
import org.kwakmunsu.randsome.admin.member.service.MemberAdminRepository;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.global.exception.NotFoundException;
import org.kwakmunsu.randsome.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberAdminRepositoryImpl implements MemberAdminRepository {

    private final MemberAdminJpaRepository memberJpaRepository;
    private final MemberAdminQueryDslRepository memberAdminQueryDslRepository;

    @Override
    public Member findById(Long id) {
        return memberJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER));
    }

    @Override
    public MemberListResponse findAllByPagination(int page) {
        return memberAdminQueryDslRepository.findAllByPagination(page);
    }

    @Override
    public long count() {
        return memberJpaRepository.count();
    }

}
