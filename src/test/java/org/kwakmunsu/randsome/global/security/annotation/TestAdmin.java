package org.kwakmunsu.randsome.global.security.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.kwakmunsu.randsome.global.security.TestAdminSecurityContextFactory;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestAdminSecurityContextFactory.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface TestAdmin {

    long id() default 1L;

    String role() default "ADMIN";

}