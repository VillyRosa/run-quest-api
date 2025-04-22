package com.runquest.api.controller;

import com.runquest.api.domain.race.NewRaceDTO;
import com.runquest.api.domain.race.RaceResponseDTO;
import com.runquest.api.domain.race.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/races")
public class RaceController {
    @Autowired
    private RaceService raceService;

    @GetMapping
    public ResponseEntity<Page<RaceResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok().body(raceService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(raceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RaceResponseDTO> save(@RequestBody NewRaceDTO newRace) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(raceService.save(newRace));
    }
}
