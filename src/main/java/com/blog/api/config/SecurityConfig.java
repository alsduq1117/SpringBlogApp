package com.blog.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers("/error")
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login").permitAll() // "/auth/login" 경로는 인증 없이 접근 가능
                        .anyRequest().authenticated()) // 그 외 모든 요청은 인증 필요
                .formLogin(form -> form
                        .loginPage("/auth/login") // 로그인 페이지 URL 설정
                        .loginProcessingUrl("/auth/login") // 로그인 처리 URL 설정 (폼 action과 일치)
                        .usernameParameter("username") // username 파라미터 이름 설정
                        .passwordParameter("password") // password 파라미터 이름 설정
                        .defaultSuccessUrl("/") // 로그인 성공 시 리다이렉트 URL 설정
                )
                .userDetailsService(userDetailsService()) // UserDetailsService 빈 등록 (인증 정보 로드)
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (개발 편의를 위해, 실제 서비스에서는 보안 취약점 발생 가능성 있음)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        UserDetails user = User
                .withUsername("minyeob")
                .password("1234")
                .roles("ADMIN")
                .build();
        manager.createUser(user);
        return manager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
