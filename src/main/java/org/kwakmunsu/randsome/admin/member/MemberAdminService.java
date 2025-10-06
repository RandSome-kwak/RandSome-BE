package org.kwakmunsu.randsome.admin.member;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberListResponse;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberAdminService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDetailResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId);

        return MemberDetailResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberListResponse getMembers(int page) {
        return memberRepository.findAll(page);
    }

}