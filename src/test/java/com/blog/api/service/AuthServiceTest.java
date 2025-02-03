package com.blog.api.service;

import com.blog.api.crypto.PasswordEncoder;
import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsEmailException;
import com.blog.api.exception.InvalidSigninInformation;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import com.blog.api.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

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
        assertEquals("김민엽", user.getName());
        assertNotNull(user.getPassword());
        assertTrue(encoder.matches("1234", user.getPassword()));
        assertEquals("alsduq1117@naver.com", user.getEmail());

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

    @Test
    @DisplayName("로그인 성공")
    void test3() {
        //given
        String ecnryptedPassword = encoder.encrpyt("1234");

        User user = User.builder()
                .email("alsduq1117@naver.com")
                .password(ecnryptedPassword)
                .name("김민엽")
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("alsduq1117@naver.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.signin(login);

        // then
        assertNotNull(userId);

    }

    @Test
    @DisplayName("로그인시 비밀번호 틀림")
    void test4() {
        //given
        String encryptedPassword = encoder.encrpyt("1234");

        User user = User.builder()
                .email("alsduq1117@naver.com")
                .password(encryptedPassword)
                .name("민엽")
                .build();
        userRepository.save(user);

        //when
        Login login = Login.builder()
                .email("alsduq1117@naver.com")
                .password("1111")
                .build();

        //then
        Assertions.assertThrows(InvalidSigninInformation.class,
                () -> authService.signin(login));

    }


}