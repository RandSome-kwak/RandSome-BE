package org.kwakmunsu.randsome.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.auth.serivce.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

}