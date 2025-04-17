package com.runquest.api.domain.auth;

import com.runquest.api.domain.user.User;
import com.runquest.api.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public User register(RegisterDTO newUser) {
        if (!newUser.password().equals(newUser.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        return userRepository.save(new User(newUser));
    }

    public String login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.email()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.getPassword().equals(loginDTO.password())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generateToken(user.getId(), user.getUsername());
    }
}
