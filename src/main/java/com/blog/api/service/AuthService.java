package com.blog.api.service;

import com.blog.api.domain.Session;
import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsEmailException;
import com.blog.api.exception.InvalidSigninInformation;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import com.blog.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(16, 8, 1, 32, 64);
        String encryptedPassword = encoder.encode(signup.getPassword());


        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
