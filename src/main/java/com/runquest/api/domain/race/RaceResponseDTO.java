package com.runquest.api.domain.race;

import com.runquest.api.domain.user.PublicUserDTO;
import com.runquest.api.domain.user.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record RaceResponseDTO(
    UUID id,
    double distance,
    int duration,
    LocalDateTime startTime,
    LocalDateTime endTime,
    PublicUserDTO User,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public RaceResponseDTO(Race race) {
        this(race.getId(), race.getDistance(), race.getDuration(), race.getStartTime(), race.getEndTime(), new PublicUserDTO(race.getUser()), race.getCreatedAt(), race.getUpdatedAt());
    }
}
