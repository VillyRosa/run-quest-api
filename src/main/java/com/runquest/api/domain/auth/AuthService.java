package com.runquest.api.domain.auth;

import com.runquest.api.domain.user.User;
import com.runquest.api.domain.user.UserRepository;
import com.runquest.api.infrastructure.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private static final String RESET_CODE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RESET_CODE_LENGTH = 6;

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

    @Transactional
    public void resetPasswordRequest(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        String rawCode = this.generateResetCode();
        String hashedCode = this.passwordEncoder.encode(rawCode);

        passwordResetTokenRepository.deleteByUserId(user.getId());
        passwordResetTokenRepository.save(new PasswordResetToken(user, hashedCode));

        emailService.send(user.getEmail(), "Alteração de senha", "Seu codigo de alteração de senha: " + rawCode);
    }

    public String resetPasswordVerify(ResetPasswordVerifyDTO verify) {
        User user = userRepository.findByEmail(verify.email()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        passwordResetToken.setAttempts(passwordResetToken.getAttempts() + 1);
        passwordResetTokenRepository.save(passwordResetToken);

        if (passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        if (!passwordEncoder.matches(verify.code(), passwordResetToken.getCodeHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generateResetToken(user.getId(), user.getUsername());
    }

    public void resetPasswordConfirm(UpdatePasswordDTO newPassword) {
        if (!newPassword.password().equals(newPassword.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        UUID id = this.getAuthenticatedUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        String hashedPassword = this.passwordEncoder.encode(newPassword.password());

        user.setPassword(hashedPassword);
        userRepository.save(user);

        passwordResetTokenRepository.deleteByUserId(id);
    }

    private String generateResetCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < RESET_CODE_LENGTH; i++) {
            int index = random.nextInt(RESET_CODE_ALPHABET.length());
            code.append(RESET_CODE_ALPHABET.charAt(index));
        }

        return code.toString();
    }
}
