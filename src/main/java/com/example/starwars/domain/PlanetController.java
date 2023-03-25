package com.example.starwars.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planetes")
public class PlanetController {

    private PlanetService planetService;

    public PlanetController(PlanetService planetService){
        this.planetService = planetService;
    }

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet){
        Planet planetCreated = this.planetService.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> get(@PathVariable("id") Long id){
        return this.planetService
                .get(id)
                .map(planet -> ResponseEntity.ok(planet))
                .orElseGet(()-> ResponseEntity.notFound().build());
    }


}
