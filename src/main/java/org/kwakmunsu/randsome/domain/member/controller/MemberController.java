package org.kwakmunsu.randsome.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberRegisterRequest;
import org.kwakmunsu.randsome.domain.member.serivce.MemberService;
import org.kwakmunsu.randsome.domain.member.serivce.dto.CheckResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Override
    @GetMapping("/check-login-id")
    public ResponseEntity<CheckResponse> checkLoginId(@RequestParam String loginId) {
        CheckResponse response = memberService.isLoginIdAvailable(loginId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/check-nickname")
    public ResponseEntity<CheckResponse> checkNickname(@RequestParam String nickname) {
        CheckResponse response = memberService.isNicknameAvailable(nickname);

        return  ResponseEntity.ok(response);
    }

}