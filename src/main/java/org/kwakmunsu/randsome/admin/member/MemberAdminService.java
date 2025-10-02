package org.kwakmunsu.randsome.admin.member;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.serivce.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberAdminService {

    private final MemberRepository memberRepository;

    public MemberDetailResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId);

        return MemberDetailResponse.from(member);
    }

}