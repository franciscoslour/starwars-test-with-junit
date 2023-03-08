package com.example.starwars.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {

    private PlanetRespository planetRespository;

    public PlanetService(PlanetRespository planetRespository){
        this.planetRespository = planetRespository;
    }

    public Planet create(Planet planet){
        return planetRespository.save(planet);
    }

}
