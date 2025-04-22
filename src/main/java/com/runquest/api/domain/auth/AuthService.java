package com.runquest.api.domain.auth;

import com.runquest.api.domain.user.User;
import com.runquest.api.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterDTO newUser) {
        if (!newUser.password().equals(newUser.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        String hashedPassword = this.passwordEncoder.encode(newUser.password());

        return userRepository.save(new User(newUser, hashedPassword));
    }

    public String login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.email()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generateToken(user.getId(), user.getUsername());
    }

    public UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UUID)) {
            throw new RuntimeException("User is not authenticated");
        }

        return (UUID) authentication.getPrincipal();
    }
}
