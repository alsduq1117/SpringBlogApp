package com.blog.api.controller;

import com.blog.api.request.Login;
import com.blog.api.response.SessionResponse;
import com.blog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Login login) {
        String acessToken = authService.signin(login);
        ResponseCookie cookie = ResponseCookie.from("SESSION", acessToken)
                .domain("localhost") // todo 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        log.info(">>>>>>> cookie = {}", cookie.toString());


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
