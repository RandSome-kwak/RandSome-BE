package org.kwakmunsu.randsome.global.security;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.randsome.global.security.annotation.TestAdmin;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@Slf4j
public class TestAdminSecurityContextFactory implements WithSecurityContextFactory<TestAdmin> {

    @Override
    public SecurityContext createSecurityContext(TestAdmin annotation) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + annotation.role());
        String memberId = String.valueOf(annotation.id());

        log.info("authorId: {}, role: {}", memberId, annotation.role());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                memberId,
                null,
                Collections.singletonList(authority)
        );
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

}