package com.example.starwars.domain;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanetService {

    private PlanetRespository planetRepository;

    public PlanetService(PlanetRespository planetRespository){
        this.planetRepository = planetRespository;
    }

    public Planet create(Planet planet){
        return planetRepository.save(planet);
    }

    public Optional<Planet> get(Long id) {
        return planetRepository.findById(id);
    }

    public Optional<Planet> findByName(String name) {
        return planetRepository.findByName(name);
    }

}
