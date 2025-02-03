package com.blog.api.service;

import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsEmailException;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;



    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }


        User user = User.builder().name(signup.getName()).email(signup.getEmail()).build();
        userRepository.save(user);
    }
}
