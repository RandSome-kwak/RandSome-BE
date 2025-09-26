package org.kwakmunsu.randsome.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberRegisterRequest;
import org.kwakmunsu.randsome.domain.member.serivce.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@RestController
public class MemberController extends MemberDocsController {

    private final MemberService memberService;

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<Long> register(@Valid @RequestBody MemberRegisterRequest request) {
        Long memberId = memberService.register(request.toServiceRequest());

        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

}