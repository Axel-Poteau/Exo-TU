package com.example.bdd;

import com.example.repository.CompteRepository;
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

public class CompteStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompteRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult derniereReponse;

    @Before
    public void reinitialiser() {
        repository.viderTout();
    }

    private MvcResult creerCompte(String numero, String titulaire) throws Exception {
        return mockMvc.perform(post("/api/comptes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": \"" + numero + "\", \"titulaire\": \"" + titulaire + "\"}"))
                .andReturn();
    }

    private MvcResult deposer(String numero, double montant) throws Exception {
        return mockMvc.perform(post("/api/comptes/" + numero + "/depot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\": " + montant + "}"))
                .andReturn();
    }

    @Given("un compte {string} pour {string}")
    public void unComptePour(String numero, String titulaire) throws Exception {
        creerCompte(numero, titulaire);
    }

    @Given("le compte {string} a un solde de {double}")
    public void leCompteAUnSoldeDe(String numero, double montant) throws Exception {
        deposer(numero, montant);
    }

    @When("je crée un compte {string} pour {string}")
    public void jeCreeUnCompte(String numero, String titulaire) throws Exception {
        derniereReponse = creerCompte(numero, titulaire);
    }

    @When("je dépose {double} sur le compte {string}")
    public void jeDepose(double montant, String numero) throws Exception {
        derniereReponse = deposer(numero, montant);
    }

    @When("je retire {double} du compte {string}")
    public void jeRetire(double montant, String numero) throws Exception {
        derniereReponse = mockMvc.perform(post("/api/comptes/" + numero + "/retrait")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\": " + montant + "}"))
                .andReturn();
    }

    @When("je vire {double} du compte {string} vers le compte {string}")
    public void jeVire(double montant, String source, String destination) throws Exception {
        derniereReponse = mockMvc.perform(post("/api/comptes/virement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"source\": \"" + source + "\", \"destination\": \"" + destination + "\", \"montant\": " + montant + "}"))
                .andReturn();
    }

    @Then("la réponse a le statut HTTP {int}")
    public void laReponseALeStatutHttp(int code) {
        assertEquals(code, derniereReponse.getResponse().getStatus());
    }

    @Then("le solde du compte {string} est {double}")
    public void leSoldeDuCompteEst(String numero, double attendu) throws Exception {
        MvcResult reponse = mockMvc.perform(get("/api/comptes/" + numero)).andReturn();
        double solde = objectMapper.readTree(reponse.getResponse().getContentAsString()).get("solde").asDouble();
        assertEquals(attendu, solde, 0.001);
    }
}
