package org.kwakmunsu.randsome.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.member.serivce.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final MemberService memberService;

}