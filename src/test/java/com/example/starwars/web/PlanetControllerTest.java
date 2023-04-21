package com.example.starwars.web;

import static com.example.starwars.common.PlanetConstants.PLANET;

import com.example.starwars.domain.Planet;
import com.example.starwars.domain.PlanetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

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

}
