package com.runquest.api.domain.race;

import com.runquest.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "races")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private double distance;
    private int duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Race(NewRaceDTO newRace, User user) {
        this.distance = newRace.distance();
        this.duration = newRace.duration();
        this.startTime = newRace.startTime();
        this.endTime = newRace.endTime();
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
