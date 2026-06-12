package com.example.controller;

import com.example.exception.TicketIntrouvableException;
import com.example.exception.TransitionInterditeException;
import com.example.exception.ValidationException;
import com.example.model.Priorite;
import com.example.model.Statut;
import com.example.model.Ticket;
import com.example.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService service;

    private Ticket unTicket(Long id, String titre, Priorite priorite, Statut statut) {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setTitre(titre);
        ticket.setPriorite(priorite);
        ticket.setStatut(statut);
        return ticket;
    }

    @Test
    void devraitRetourner201ALaCreationDunTicket() throws Exception {
        when(service.creerTicket("Ecran bleu au demarrage", Priorite.HIGH))
                .thenReturn(unTicket(1L, "Ecran bleu au demarrage", Priorite.HIGH, Statut.OPEN));

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\": \"Ecran bleu au demarrage\", \"priorite\": \"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Ecran bleu au demarrage"))
                .andExpect(jsonPath("$.statut").value("OPEN"));
    }

    @Test
    void devraitRetourner400QuandLaValidationEchoue() throws Exception {
        when(service.creerTicket(anyString(), any()))
                .thenThrow(new ValidationException("Le titre doit contenir au moins 3 caracteres"));

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\": \"ab\", \"priorite\": \"LOW\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Le titre doit contenir au moins 3 caracteres"));
    }

    @Test
    void devraitRetournerUnTicketParSonId() throws Exception {
        when(service.trouverParId(1L)).thenReturn(unTicket(1L, "Imprimante en panne", Priorite.MEDIUM, Statut.OPEN));

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Imprimante en panne"))
                .andExpect(jsonPath("$.priorite").value("MEDIUM"));
    }

    @Test
    void devraitRetourner404QuandLeTicketNexistePas() throws Exception {
        when(service.trouverParId(42L)).thenThrow(new TicketIntrouvableException("Le ticket 42 n'existe pas"));

        mockMvc.perform(get("/api/tickets/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Le ticket 42 n'existe pas"));
    }

    @Test
    void devraitListerLesTickets() throws Exception {
        when(service.listerTickets()).thenReturn(List.of(
                unTicket(1L, "Imprimante en panne", Priorite.MEDIUM, Statut.OPEN),
                unTicket(2L, "Mot de passe oublie", Priorite.LOW, Statut.RESOLVED)));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titre").value("Imprimante en panne"));
    }

    @Test
    void devraitModifierLeStatutDunTicket() throws Exception {
        when(service.changerStatut(1L, Statut.RESOLVED))
                .thenReturn(unTicket(1L, "Imprimante en panne", Priorite.MEDIUM, Statut.RESOLVED));

        mockMvc.perform(put("/api/tickets/1/statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\": \"RESOLVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("RESOLVED"));
    }

    @Test
    void devraitRetourner409QuandLaTransitionEstInterdite() throws Exception {
        when(service.changerStatut(1L, Statut.IN_PROGRESS))
                .thenThrow(new TransitionInterditeException("Un ticket resolu ne peut plus changer de statut"));

        mockMvc.perform(put("/api/tickets/1/statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\": \"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Un ticket resolu ne peut plus changer de statut"));
    }
}
