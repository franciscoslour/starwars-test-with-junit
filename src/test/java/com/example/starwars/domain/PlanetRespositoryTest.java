package com.example.starwars.domain;

import static com.example.starwars.common.PlanetConstants.PLANET;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
public class PlanetRespositoryTest {

    @Autowired
    private PlanetRespository planetRespository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void creatPlanet_WithValidData_ReturnsPlanet(){

        Planet planet = this.planetRespository.save(PLANET);
        Planet sut = this.testEntityManager.find(Planet.class, planet.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void createPlanet_WithInvalidDate_ThrowsException(){
        Planet empetyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        assertThatThrownBy(()-> planetRespository.save(empetyPlanet));
       // assertThatThrownBy(()-> planetRespository.save(invalidPlanet));
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException(){
        Planet planet = this.testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRespository.save(planet)).isInstanceOf(RuntimeException.class);

    }


}
