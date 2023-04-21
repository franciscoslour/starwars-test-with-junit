package com.example.starwars.web;

import com.example.starwars.domain.Planet;
import com.example.starwars.domain.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.starwars.common.PlanetConstants.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlanetService planetService;

    @Test
    public void creatPlanet_withValidData_ReturnsCreated() throws Exception {

        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(
                    post("/planets")
                        .content(this.objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));
    }


    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {

        Planet empetyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        mockMvc.perform(
                        post("/planets")
                                .content(this.objectMapper.writeValueAsString(empetyPlanet))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(
                        post("/planets")
                                .content(this.objectMapper.writeValueAsString(invalidPlanet))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    public void creatPlanet_WithExistingName_ReturnsConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(
                        post("/planets")
                                .content(this.objectMapper.writeValueAsString(PLANET))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());

    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {

        when(planetService.get(any())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));


    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty() throws Exception {

        when(planetService.get(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception{

        when(planetService.findByName(any())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/name/name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));

    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsNotFound() throws Exception{
        when(planetService.findByName(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/name/name"))
                .andExpect(status().isNotFound());
    }

    @Test
    public  void listPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(PLANETS);
        when(planetService.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));

        mockMvc
                .perform(
                    get("/planets")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc
                .perform(
                        get("/planets")
                                .queryParam("terrain",TATOOINE.getTerrain())
                                .queryParam("climate",TATOOINE.getClimate())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(TATOOINE));
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(Collections.emptyList());

        mockMvc
                .perform(
                        get("/planets")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    public void removePlanet_WithExistingId_RemovesPlanetFromDatabase() throws Exception {
       mockMvc
               .perform(
                       delete("/planets/1")
               )
               .andExpect(status().isNoContent());
    }

    @Test
    public void removePlanet_WithUnexistingId_ReturnsNotFound() throws Exception{
        final long planetId = 1L;
        doThrow(new EmptyResultDataAccessException(1))
                .when(planetService)
                .remove(planetId);

        mockMvc
                .perform(
                    delete("/planets/"+planetId)
                )
                .andExpect(status().isNotFound());

    }

}
