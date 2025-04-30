package com.runquest.api.controller;

import com.runquest.api.domain.auth.*;
import com.runquest.api.domain.user.User;
import com.runquest.api.domain.user.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterDTO newUser) {
        User user = authService.register(newUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserResponseDTO(user));
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<Void> resetPasswordRequest(@RequestBody ResetPasswordRequestDTO request) {
        authService.resetPasswordRequest(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password/verify")
    public ResponseEntity<String> resetPasswordVerify(@RequestBody ResetPasswordVerifyDTO verify) {
        return ResponseEntity.ok(authService.resetPasswordVerify(verify));
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<Void> resetPasswordConfirm(@RequestBody UpdatePasswordDTO newPassword) {
        authService.resetPasswordConfirm(newPassword);
        return ResponseEntity.ok().build();
    }
}
