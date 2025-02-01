package com.blog.api.service;

import com.blog.api.domain.Session;
import com.blog.api.domain.User;
import com.blog.api.exception.InvalidSigninInformation;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        Session session = user.addSession();
        return user.getId();

    }
}
