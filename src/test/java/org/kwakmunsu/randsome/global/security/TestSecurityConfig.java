package org.kwakmunsu.randsome.global.security;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.member.enums.Role;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/members/sign-up",
                                "/api/v1/members/check-login-id",
                                "/api/v1/members/check-nickname"
                                ).permitAll()   // 로그인, 회원가입, 토큰 재발급 등은 인증 불필요
                        .requestMatchers("/api/v1/admin/**").hasAuthority(Role.ROLE_ADMIN.name())
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}