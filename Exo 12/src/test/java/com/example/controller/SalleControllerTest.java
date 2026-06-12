package com.example.controller;

import com.example.exception.ValidationException;
import com.example.model.Salle;
import com.example.service.SalleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalleController.class)
class SalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalleService service;

    private Salle uneSalle(Long id, String nom, int capacite) {
        Salle salle = new Salle();
        salle.setId(id);
        salle.setNom(nom);
        salle.setCapacite(capacite);
        return salle;
    }

    @Test
    void devraitCreerUneSalle() throws Exception {
        when(service.creerSalle("Salle A", 8)).thenReturn(uneSalle(1L, "Salle A", 8));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\": \"Salle A\", \"capacite\": 8}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Salle A"));
    }

    @Test
    void devraitRetourner400QuandLaSalleEstInvalide() throws Exception {
        when(service.creerSalle(anyString(), anyInt()))
                .thenThrow(new ValidationException("La capacite doit etre superieure ou egale a 1"));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\": \"Salle A\", \"capacite\": 0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La capacite doit etre superieure ou egale a 1"));
    }

    @Test
    void devraitListerLesSalles() throws Exception {
        when(service.listerSalles()).thenReturn(List.of(
                uneSalle(1L, "Salle A", 8),
                uneSalle(2L, "Salle B", 4)));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].nom").value("Salle B"));
    }
}
