package com.example.starwars.domain;

import static com.example.starwars.common.PlanetConstants.INVALID_PLANET;
import static com.example.starwars.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = PlanetService.class)
public class PlanetServiceTest {

    //@Autowired
    @InjectMocks
    private PlanetService planetService;
   // @MockBean
    @Mock
    private PlanetRespository planetRespository;

    @Test
    public void createPlanet_WithValidDate_ReturnsPlanet(){
       when(planetRespository.save(PLANET)).thenReturn(PLANET);

       Planet sut = planetService.create(PLANET);

       assertThat(sut).isEqualTo(PLANET);

    }

    @Test
    public void creatPlanet_WithInvalidData_ThrowException(){

        when(planetRespository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(()-> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(this.planetRespository.findById(anyLong())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = this.planetService.get(1L);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);

    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty() {
        when(this.planetRespository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Planet> sut = this.planetService.get(1L);

        assertThat(sut).isEmpty();
    }


    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
       when(this.planetRespository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

       Optional<Planet> sut = this.planetService.findByName(PLANET.getName());

       assertThat(sut).isNotEmpty();
       assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsEmpty() {
        when(this.planetRespository.findByName("")).thenReturn(Optional.empty());

        Optional<Planet> sut = this.planetService.findByName("");

        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        List<Planet> planets = List.of(PLANET);
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));
        when(this.planetRespository.findAll(query)).thenReturn(planets);

        List<Planet> sut = this.planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        when(this.planetRespository.findAll(any())).thenReturn(Collections.emptyList());

        List<Planet> sut = this.planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isEmpty();

    }

    @Test
    public void removePlanet_WithExistingId_doesNotThrowAnyException() {
       assertThatCode(() -> this.planetService.remove(anyLong())).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithUnexistingId_ThrowsException() {
        doThrow(new RuntimeException()).when(planetRespository).deleteById(anyLong());
        assertThatThrownBy(() -> this.planetService.remove(anyLong())).isInstanceOf(RuntimeException.class);
    }



}
