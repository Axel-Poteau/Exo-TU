package com.example.bdd;

import com.example.repository.ReservationRepository;
import com.example.repository.SalleRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ReservationStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private long salleId;
    private MvcResult derniereReponse;

    @Before
    public void reinitialiser() {
        reservationRepository.viderTout();
        salleRepository.viderTout();
    }

    private MvcResult reserver(long salleId, String nom, String debut, String fin) throws Exception {
        return mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"salleId\": " + salleId + ", \"nomPersonne\": \"" + nom + "\", "
                                + "\"debut\": \"" + debut + "\", \"fin\": \"" + fin + "\"}"))
                .andReturn();
    }

    @Given("une salle {string} avec une capacité de {int}")
    public void uneSalleAvecUneCapacite(String nom, int capacite) throws Exception {
        MvcResult creation = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\": \"" + nom + "\", \"capacite\": " + capacite + "}"))
                .andReturn();
        salleId = objectMapper.readTree(creation.getResponse().getContentAsString()).get("id").asLong();
    }

    @Given("une réservation existante pour {string} du {string} au {string}")
    public void uneReservationExistante(String nom, String debut, String fin) throws Exception {
        reserver(salleId, nom, debut, fin);
    }

    @When("je réserve cette salle pour {string} du {string} au {string}")
    public void jeReserveCetteSalle(String nom, String debut, String fin) throws Exception {
        derniereReponse = reserver(salleId, nom, debut, fin);
    }

    @When("je réserve la salle {int} pour {string} du {string} au {string}")
    public void jeReserveLaSalle(int id, String nom, String debut, String fin) throws Exception {
        derniereReponse = reserver(id, nom, debut, fin);
    }

    @Then("la réponse a le statut HTTP {int}")
    public void laReponseALeStatutHttp(int code) {
        assertEquals(code, derniereReponse.getResponse().getStatus());
    }

    @Then("la réservation retournée a le statut {string}")
    public void laReservationRetourneeALeStatut(String statut) throws Exception {
        String corps = derniereReponse.getResponse().getContentAsString();
        assertEquals(statut, objectMapper.readTree(corps).get("statut").asText());
    }
}
