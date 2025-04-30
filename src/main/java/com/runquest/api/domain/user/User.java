package com.runquest.api.domain.user;

import com.runquest.api.domain.auth.RegisterDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "users")
@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String email;
    private LocalDateTime emailVerifiedAt;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(RegisterDTO newUser, String hashedPassword) {
        this.username = newUser.username();
        this.email = newUser.email();
        this.password = hashedPassword;
        this.status = UserStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
