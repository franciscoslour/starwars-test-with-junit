package com.example.starwars.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.example.starwars.common.PlanetConstants.PLANET;
import static com.example.starwars.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
public class PlanetRespositoryTest {

    @Autowired
    private PlanetRespository planetRespository;
    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach(){
        PLANET.setId(null);
    }

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

    @Test
    public void getPlanetByExistingId_ReturnsPlanet(){
        Planet planet = this.testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> planetOpt = planetRespository.findById(planet.getId());

        assertThat(planetOpt).isNotEmpty();
        assertThat(planetOpt.get()).isEqualTo(planet);

    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty(){
        Optional<Planet> planetOpt = planetRespository.findById(1L);

        assertThat(planetOpt).isEmpty();

    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet(){
        Planet planet = this.testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> planetOpt = planetRespository.findByName(planet.getName());

        assertThat(planetOpt).isNotEmpty();
        assertThat(planetOpt.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsNotFound(){
        Optional<Planet> planetOpt = planetRespository.findByName(PLANET.getName());
        assertThat(planetOpt).isEmpty();
    }

    @Test
    @Sql(scripts = "/import_planets.sql")
    public void listPlanets_ReturnsFilteredPlanets(){
        Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> quertWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

        List<Planet> responseWithoutFilters = planetRespository.findAll(queryWithoutFilters);
        List<Planet> responseWithFilters = planetRespository.findAll(quertWithFilters);

        assertThat(responseWithoutFilters).isNotEmpty();
        assertThat(responseWithoutFilters).hasSize(3);
        assertThat(responseWithFilters).isNotEmpty();
        assertThat(responseWithFilters).hasSize(1);
        assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
    }

}
