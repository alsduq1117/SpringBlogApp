package com.blog.api.controller;

import com.blog.api.domain.Session;
import com.blog.api.domain.User;
import com.blog.api.repository.SessionRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {
        // given
        userRepository.save(User.builder().email("alsduq1117@gmail.com").name("김민엽").password("1234").build());

        Login login = Login.builder().email("alsduq1117@gmail.com").password("1234").build();

        String json = objectMapper.writeValueAsString(login);

        // when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void test2() throws Exception {
        // given
        User user = userRepository.save(User.builder().email("alsduq1117@gmail.com").name("김민엽").password("1234").build());

        Login login = Login.builder().email("alsduq1117@gmail.com").password("1234").build();

        String json = objectMapper.writeValueAsString(login);

        // when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1L, user.getSessions().size());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception {
        // given
        User user = userRepository.save(User.builder().email("alsduq1117@gmail.com").name("김민엽").password("1234").build());

        Login login = Login.builder().email("alsduq1117@gmail.com").password("1234").build();

        String json = objectMapper.writeValueAsString(login);

        // when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
    void test4() throws Exception {
        // given
        User user = User.builder()
                .email("alsduq1117@gmail.com")
                .name("김민엽")
                .password("1234")
                .build();

        Session session = user.addSession();
        userRepository.save(user);


        // when,then
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속 할 수 없다.")
    void test5() throws Exception {
        // given
        User user = User.builder()
                .email("alsduq1117@gmail.com")
                .name("김민엽")
                .password("1234")
                .build();

        Session session = user.addSession();
        userRepository.save(user);


        // when,then
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .header("Authorization", session.getAccessToken() + "-O")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }
}