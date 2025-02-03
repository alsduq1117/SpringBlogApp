package com.blog.api.controller;

import com.blog.api.repository.UserRepository;
import com.blog.api.request.Signup;
import com.fasterxml.jackson.databind.ObjectMapper;
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


    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {
        // given
        Signup signup = Signup.builder()
                .name("minyeob")
                .password("1234")
                .email("alsduq1117@naver.com")
                .build();


        // when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}