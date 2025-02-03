package com.blog.api.service;

import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsEmailException;
import com.blog.api.exception.PostNotFound;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Signup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
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
    void clean() { userRepository.deleteAll();}

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        //given
        Signup signup = Signup.builder()
                .name("minyeob")
                .password("1234")
                .email("alsduq1117@naver.com")
                .build();


        //when
        authService.signup(signup);


        //then
        Assertions.assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("minyeob", user.getName());
        assertNotNull(user.getPassword());
        assertNotEquals("1234", user.getPassword());
        assertEquals("alsduq1117@naver.com", user.getEmail());

    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        //given
        User user = User.builder()
                .email("alsduq1117@naver.com")
                .password("1234")
                .name("민엽")
                .build();

        userRepository.save(user);

        Signup signup = Signup.builder()
                .name("minyeob")
                .password("1234")
                .email("alsduq1117@naver.com")
                .build();

        //when, then
        Assertions.assertThrows(AlreadyExistsEmailException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                authService.signup(signup);
            }
        });
    }

}