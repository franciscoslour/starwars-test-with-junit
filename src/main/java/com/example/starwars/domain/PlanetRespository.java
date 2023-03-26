package com.example.starwars.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlanetRespository extends CrudRepository<Planet, Long> {
    Optional<Planet> findByName(String name);
}
