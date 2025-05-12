package com.runquest.api.controller;

import com.runquest.api.domain.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<PublicUserDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok().body(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicUserDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyAccount() {
        return ResponseEntity.ok().body(userService.findMyAccount());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyAccount(@RequestBody UpdateUserDTO updateUser) {
        return ResponseEntity.ok().body(userService.updateMyAccount(updateUser));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updateMyPassword(@RequestBody UpdatePasswordDTO newPassword) {
        userService.updateMyPassword(newPassword);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount() {
        userService.deleteMyAccount();
        return ResponseEntity.noContent().build();
    }
}
