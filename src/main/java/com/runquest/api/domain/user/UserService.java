package com.runquest.api.domain.user;

import com.runquest.api.domain.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDTO::new);
    }

    public UserResponseDTO findById(UUID id) {
        return userRepository.findById(id).map(UserResponseDTO::new).orElse(null);
    }

    public UserResponseDTO findMyAccount() {
        UUID id = authService.getAuthenticatedUserId();
        return this.findById(id);
    }

    public UserResponseDTO updateMyAccount(UpdateUserDTO updateUser) {
        UUID id = authService.getAuthenticatedUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(updateUser.username());
        user.setEmail(updateUser.email());

        return new UserResponseDTO(userRepository.save(user));
    }

    public void updateMyPassword(UpdatePasswordDTO newPassword) {
        if (!newPassword.newPassword().equals(newPassword.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        UUID id = authService.getAuthenticatedUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(newPassword.password(), user.getPassword())) {
            throw new IllegalArgumentException("Actual password does not match");
        }

        String hashedPassword = this.passwordEncoder.encode(newPassword.newPassword());

        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public void deleteMyAccount() {
        UUID id = authService.getAuthenticatedUserId();
        userRepository.deleteById(id);
    }
}
