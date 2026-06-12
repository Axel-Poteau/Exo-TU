package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void parcoursCompletDeReservation() throws Exception {
        MvcResult creationSalle = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\": \"Salle du conseil\", \"capacite\": 12}"))
                .andExpect(status().isCreated())
                .andReturn();

        long salleId = objectMapper.readTree(creationSalle.getResponse().getContentAsString()).get("id").asLong();

        MvcResult creationReservation = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"salleId\": " + salleId + ", \"nomPersonne\": \"Axel\", "
                                + "\"debut\": \"2026-06-15T10:00:00\", \"fin\": \"2026-06-15T11:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"))
                .andReturn();

        long reservationId = objectMapper.readTree(creationReservation.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomPersonne").value("Axel"));

        mockMvc.perform(patch("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("ANNULEE"));
    }
}
