package com.runquest.api.domain.auth;

public record RegisterDTO(
    String username,
    String email,
    String password,
    String confirmPassword
) {}
