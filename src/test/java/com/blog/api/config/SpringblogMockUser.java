package com.blog.api.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = SpringblogMockSecurityContext.class)
public @interface SpringblogMockUser {

    String name() default "김민엽";

    String email() default "alsduq1117@naver.com";

    String password() default "1234";

//    String role() default "ROLE_ADMIN";
}
