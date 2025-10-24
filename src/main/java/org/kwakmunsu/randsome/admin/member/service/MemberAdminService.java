package org.kwakmunsu.randsome.admin.member.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.member.repository.MemberAdminRepository;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.kwakmunsu.randsome.domain.EntityStatus;
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
    public PageResponse<MemberDetailResponse> getMembers(PageRequest request) {
        List<Member> members = memberRepository.findAllByStatus(request.offset(), request.limit(), EntityStatus.ACTIVE);
        long count = memberRepository.countByStatus(EntityStatus.ACTIVE);

        return new PageResponse<>(
                members.stream()
                        .map(MemberDetailResponse::from)
                        .toList(),
                count
        );
    }

}