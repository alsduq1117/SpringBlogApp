package com.blog.api.config;

import com.blog.api.config.data.UserSession;
import com.blog.api.exception.Unauthorized;
import com.blog.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final String KEY = "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";


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");
        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        byte[] decodedKey = Base64.getDecoder().decode(KEY);

        try {       //JWT 복호화 과정
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(decodedKey).build().parseClaimsJws(jws);

            String userId = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(userId));
        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }
}
