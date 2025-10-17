package org.kwakmunsu.randsome.admin.member.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.member.repository.dto.MemberListResponse;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberAdminService {

    private final MemberAdminRepository memberRepository;

    public MemberDetailResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId);

        return MemberDetailResponse.from(member);
    }

    // TODO: 추후 정렬, 검색 기능 추가
    public MemberListResponse getMemberList(int page) {
        return memberRepository.findAllByPagination(page);
    }

}