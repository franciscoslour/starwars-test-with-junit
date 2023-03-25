package com.example.starwars.domain;

import static com.example.starwars.common.PlanetConstants.INVALID_PLANET;
import static com.example.starwars.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

}
