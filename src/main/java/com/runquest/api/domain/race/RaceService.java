package com.runquest.api.domain.race;

import com.runquest.api.domain.auth.AuthService;
import com.runquest.api.domain.user.User;
import com.runquest.api.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RaceService {
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    public Page<RaceResponseDTO> findAll(Pageable pageable) {
        return raceRepository.findAll(pageable).map(RaceResponseDTO::new);
    }

    public RaceResponseDTO findById(UUID id) {
        return raceRepository.findById(id).map(RaceResponseDTO::new).orElse(null);
    }

    public RaceResponseDTO save(NewRaceDTO newRace) {
        UUID userId = authService.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Race race = new Race(newRace, user);
        Race savedRace = raceRepository.save(race);

        return new RaceResponseDTO(savedRace);
    }
}
