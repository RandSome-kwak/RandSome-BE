package org.kwakmunsu.randsome.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberProfileUpdateRequest;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberRegisterRequest;
import org.kwakmunsu.randsome.domain.member.service.MemberCommandService;
import org.kwakmunsu.randsome.domain.member.service.MemberQueryService;
import org.kwakmunsu.randsome.domain.member.service.dto.CheckResponse;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberProfileResponse;
import org.kwakmunsu.randsome.global.annotation.AuthMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@RestController
public class MemberController extends MemberDocsController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<Long> register(@Valid @RequestBody MemberRegisterRequest request) {
        Long memberId = memberCommandService.register(request.toServiceRequest());

        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @Override
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> getProfile(@AuthMember Long memberId) {
        MemberProfileResponse response = memberQueryService.getProfile(memberId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @AuthMember Long memberId,
            @Valid @RequestBody MemberProfileUpdateRequest request
    ) {
        memberCommandService.updateProfile(request.toServiceRequest(memberId));

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/check-login-id")
    public ResponseEntity<CheckResponse> checkLoginId(@RequestParam String loginId) {
        CheckResponse response = memberQueryService.isLoginIdAvailable(loginId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/check-nickname")
    public ResponseEntity<CheckResponse> checkNickname(@RequestParam String nickname) {
        CheckResponse response = memberQueryService.isNicknameAvailable(nickname);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/check-instagram-id")
    public ResponseEntity<CheckResponse> checkInstagramId(@RequestParam String instagramId) {
        CheckResponse response = memberQueryService.isInstagramIdAvailable(instagramId);

        return ResponseEntity.ok(response);
    }

}