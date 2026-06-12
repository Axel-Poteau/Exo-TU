package com.example.bdd;

import com.example.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class TicketStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult derniereReponse;
    private long dernierTicketId;

    @Before
    public void reinitialiser() {
        repository.viderTout();
    }

    private MvcResult creerTicket(String titre, String priorite) throws Exception {
        return mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\": \"" + titre + "\", \"priorite\": \"" + priorite + "\"}"))
                .andReturn();
    }

    private MvcResult changerStatut(long id, String statut) throws Exception {
        return mockMvc.perform(put("/api/tickets/" + id + "/statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\": \"" + statut + "\"}"))
                .andReturn();
    }

    @Given("un ticket {string} avec la priorité {string}")
    public void unTicketAvecLaPriorite(String titre, String priorite) throws Exception {
        MvcResult creation = creerTicket(titre, priorite);
        dernierTicketId = objectMapper.readTree(creation.getResponse().getContentAsString()).get("id").asLong();
    }

    @Given("ce ticket est déjà au statut {string}")
    public void ceTicketEstDejaAuStatut(String statut) throws Exception {
        changerStatut(dernierTicketId, statut);
    }

    @When("je crée un ticket {string} avec la priorité {string}")
    public void jeCreeUnTicket(String titre, String priorite) throws Exception {
        derniereReponse = creerTicket(titre, priorite);
    }

    @When("je passe ce ticket au statut {string}")
    public void jePasseCeTicketAuStatut(String statut) throws Exception {
        derniereReponse = changerStatut(dernierTicketId, statut);
    }

    @When("je consulte le ticket {int}")
    public void jeConsulteLeTicket(int id) throws Exception {
        derniereReponse = mockMvc.perform(get("/api/tickets/" + id)).andReturn();
    }

    @Then("la réponse a le statut HTTP {int}")
    public void laReponseALeStatutHttp(int code) {
        assertEquals(code, derniereReponse.getResponse().getStatus());
    }

    @Then("le ticket retourné a le statut {string}")
    public void leTicketRetourneALeStatut(String statut) throws Exception {
        String corps = derniereReponse.getResponse().getContentAsString();
        assertEquals(statut, objectMapper.readTree(corps).get("statut").asText());
    }
}