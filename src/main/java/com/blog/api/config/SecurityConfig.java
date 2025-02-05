package com.blog.api.config;

import com.blog.api.domain.User;
import com.blog.api.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
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
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // "/auth/login" 경로는 인증 없이 접근 가능
                        .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll() // "/auth/login" 경로는 인증 없이 접근 가능
                        .anyRequest().authenticated()) // 그 외 모든 요청은 인증 필요
                .formLogin(form -> form
                        .loginPage("/auth/login") // 로그인 페이지 URL 설정
                        .loginProcessingUrl("/auth/login") // 로그인 처리 URL 설정 (폼 action과 일치)
                        .usernameParameter("username") // username 파라미터 이름 설정
                        .passwordParameter("password") // password 파라미터 이름 설정
                        .defaultSuccessUrl("/") // 로그인 성공 시 리다이렉트 URL 설정
                )
                .rememberMe(rm -> rm.rememberMeParameter("remember")    // 자동 로그인 기능
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000)
                )
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (개발 편의를 위해, 실제 서비스에서는 보안 취약점 발생 가능성 있음)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다"));
                return new UserPrincipal(user);
            }
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder(
                16,
                8,
                1,
                32,
                64
        );
    }
}
