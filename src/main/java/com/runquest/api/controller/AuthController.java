package com.runquest.api.controller;

import com.runquest.api.domain.auth.AuthService;
import com.runquest.api.domain.auth.LoginDTO;
import com.runquest.api.domain.auth.RegisterDTO;
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
}
