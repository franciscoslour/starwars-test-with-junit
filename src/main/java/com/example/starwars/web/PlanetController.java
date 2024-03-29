package com.example.starwars.web;

import com.example.starwars.domain.Planet;
import com.example.starwars.domain.PlanetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/planets")
public class PlanetController {

    private PlanetService planetService;

    public PlanetController(PlanetService planetService){
        this.planetService = planetService;
    }

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet){
        if(planet.getName() == null || planet.getName().isEmpty())
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
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

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> get(@PathVariable("name") String name){
        return this.planetService
                .findByName(name)
                .map(planet -> ResponseEntity.ok(planet))
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Planet>> list(@RequestParam(required = false) String terrain,
                                             @RequestParam(required = false) String climate){
        List<Planet> planets = this.planetService.list(terrain, climate);
        return ResponseEntity.ok(planets);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable("id") Long id) {
        planetService.remove(id);
        return ResponseEntity.noContent().build();
    }


}
