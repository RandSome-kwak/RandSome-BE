package org.kwakmunsu.randsome.admin.member.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.PageRequest;
import org.kwakmunsu.randsome.admin.PageResponse;
import org.kwakmunsu.randsome.admin.member.service.MemberAdminService;
import org.kwakmunsu.randsome.admin.member.service.dto.MemberDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/api/v1/admin/members/")
@RequiredArgsConstructor
@RestController
public class MemberAdminController extends MemberAdminDocsController {

    private final MemberAdminService memberAdminService;

    @Override
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailResponse> getMember(@PathVariable Long memberId) {
        MemberDetailResponse response = memberAdminService.getMember(memberId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<MemberDetailResponse>> getMembers(@RequestParam (defaultValue = "1") @Min(1) int page) {
        PageResponse<MemberDetailResponse> response = memberAdminService.getMembers(new PageRequest(page));

        return ResponseEntity.ok(response);
    }

}