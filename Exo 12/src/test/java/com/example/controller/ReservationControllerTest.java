package com.example.controller;

import com.example.exception.ConflitReservationException;
import com.example.exception.RessourceIntrouvableException;
import com.example.model.Reservation;
import com.example.model.StatutReservation;
import com.example.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService service;

    private Reservation uneReservation(StatutReservation statut) {
        Reservation reservation = new Reservation();
        reservation.setId(5L);
        reservation.setSalleId(1L);
        reservation.setNomPersonne("Axel");
        reservation.setDebut(LocalDateTime.of(2026, 6, 15, 10, 0));
        reservation.setFin(LocalDateTime.of(2026, 6, 15, 11, 0));
        reservation.setStatut(statut);
        return reservation;
    }

    private final String corpsValide = "{\"salleId\": 1, \"nomPersonne\": \"Axel\", "
            + "\"debut\": \"2026-06-15T10:00:00\", \"fin\": \"2026-06-15T11:00:00\"}";

    @Test
    void devraitCreerUneReservation() throws Exception {
        when(service.creerReservation(anyLong(), anyString(), any(), any()))
                .thenReturn(uneReservation(StatutReservation.CONFIRMEE));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpsValide))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"));
    }

    @Test
    void devraitRetourner404QuandLaSalleNexistePas() throws Exception {
        when(service.creerReservation(anyLong(), anyString(), any(), any()))
                .thenThrow(new RessourceIntrouvableException("La salle 99 n'existe pas"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpsValide))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("La salle 99 n'existe pas"));
    }

    @Test
    void devraitRetourner404QuandLaReservationNexistePas() throws Exception {
        when(service.trouverParId(42L)).thenThrow(new RessourceIntrouvableException("La reservation 42 n'existe pas"));

        mockMvc.perform(get("/api/reservations/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    void devraitRetourner409EnCasDeChevauchement() throws Exception {
        when(service.creerReservation(anyLong(), anyString(), any(), any()))
                .thenThrow(new ConflitReservationException("Le creneau chevauche une reservation existante"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpsValide))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Le creneau chevauche une reservation existante"));
    }

    @Test
    void devraitAnnulerUneReservation() throws Exception {
        when(service.annuler(5L)).thenReturn(uneReservation(StatutReservation.ANNULEE));

        mockMvc.perform(patch("/api/reservations/5/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("ANNULEE"));
    }
}
