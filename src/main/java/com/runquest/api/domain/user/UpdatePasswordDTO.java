package com.runquest.api.domain.user;

public record UpdatePasswordDTO(String password, String newPassword, String confirmPassword) {
}
