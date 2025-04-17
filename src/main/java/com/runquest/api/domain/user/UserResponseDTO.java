package com.runquest.api.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String username,
    String email,
    UserStatus status,
    LocalDateTime emailVerifiedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getStatus(), user.getEmailVerifiedAt(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
