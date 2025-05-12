package com.runquest.api.domain.user;

import java.util.UUID;

public record PublicUserDTO(UUID id, String username) {
    public PublicUserDTO(User user) {
        this(user.getId(), user.getUsername());
    }
}
