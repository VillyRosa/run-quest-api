package com.runquest.api.domain.auth;

public record ResetPasswordVerifyDTO(String email, String code) {
}
