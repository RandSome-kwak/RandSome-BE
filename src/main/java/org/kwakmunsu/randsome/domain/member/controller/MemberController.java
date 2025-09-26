package org.kwakmunsu.randsome.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.member.serivce.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

}