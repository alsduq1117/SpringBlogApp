package com.blog.api.service;

import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsEmailException;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AuthService authService;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        //given
        Signup signup = Signup.builder()
                .name("김민엽")
                .email("alsduq1117@naver.com")
                .password("1234")
                .build();

        authService.signup(signup);

        //then
        Assertions.assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();

    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        //given
        User user = User.builder()
                .name("김민엽")
                .email("alsduq1117@naver.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Signup signup = Signup.builder()
                .name("김민엽")
                .email("alsduq1117@naver.com")
                .password("1234")
                .build();


        // then
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));

    }


}