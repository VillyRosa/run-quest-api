package com.runquest.api.domain.race;

import java.time.LocalDateTime;

public record NewRaceDTO(
    LocalDateTime startTime,
    LocalDateTime endTime,
    Double distance,
    Integer duration
) {
}
