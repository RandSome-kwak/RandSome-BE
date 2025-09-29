package org.kwakmunsu.randsome.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.auth.controller.dto.LoginRequest;
import org.kwakmunsu.randsome.domain.auth.controller.dto.ReissueRequest;
import org.kwakmunsu.randsome.domain.auth.serivce.AuthService;
import org.kwakmunsu.randsome.global.jwt.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController extends AuthDocsController{

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request.loginId(), request.password());

        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@Valid @RequestBody ReissueRequest request) {
        TokenResponse response = authService.reissue(request.refreshToken());

        return  ResponseEntity.ok(response);
    }

}