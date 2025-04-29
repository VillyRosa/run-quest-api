package com.runquest.api.domain.user;

import com.runquest.api.domain.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDTO::new);
    }

    public UserResponseDTO findById(UUID id) {
        return userRepository.findById(id).map(UserResponseDTO::new).orElse(null);
    }

    public void deleteMyAccount() {
        UUID id = authService.getAuthenticatedUserId();
        userRepository.deleteById(id);
    }
}
