package com.example.controller;

import com.example.exception.CompteDejaExistantException;
import com.example.exception.CompteIntrouvableException;
import com.example.exception.SoldeInsuffisantException;
import com.example.exception.ValidationException;
import com.example.model.Compte;
import com.example.service.CompteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompteController.class)
class CompteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompteService service;

    private Compte compte(String numero, String titulaire, double solde) {
        Compte compte = new Compte();
        compte.setNumero(numero);
        compte.setTitulaire(titulaire);
        compte.setSolde(solde);
        return compte;
    }

    @Test
    void devraitRetourner201ALaCreationDunCompte() throws Exception {
        when(service.creerCompte("FR001", "Alice")).thenReturn(compte("FR001", "Alice", 0));

        mockMvc.perform(post("/api/comptes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": \"FR001\", \"titulaire\": \"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value("FR001"))
                .andExpect(jsonPath("$.titulaire").value("Alice"))
                .andExpect(jsonPath("$.solde").value(0.0));
    }

    @Test
    void devraitRetourner409QuandLeNumeroExisteDeja() throws Exception {
        when(service.creerCompte(anyString(), anyString()))
                .thenThrow(new CompteDejaExistantException("Le compte FR001 existe deja"));

        mockMvc.perform(post("/api/comptes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": \"FR001\", \"titulaire\": \"Alice\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Le compte FR001 existe deja"));
    }

    @Test
    void devraitConsulterUnCompteParSonNumero() throws Exception {
        when(service.consulter("FR001")).thenReturn(compte("FR001", "Alice", 120));

        mockMvc.perform(get("/api/comptes/FR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulaire").value("Alice"))
                .andExpect(jsonPath("$.solde").value(120.0));
    }

    @Test
    void devraitRetourner404QuandLeCompteNexistePas() throws Exception {
        when(service.consulter("FR404")).thenThrow(new CompteIntrouvableException("Le compte FR404 n'existe pas"));

        mockMvc.perform(get("/api/comptes/FR404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Le compte FR404 n'existe pas"));
    }

    @Test
    void devraitListerLesComptes() throws Exception {
        when(service.listerComptes()).thenReturn(List.of(
                compte("FR001", "Alice", 0),
                compte("FR002", "Bob", 50)));

        mockMvc.perform(get("/api/comptes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].numero").value("FR001"));
    }

    @Test
    void devraitDeposerSurUnCompte() throws Exception {
        when(service.deposer("FR001", 50.0)).thenReturn(compte("FR001", "Alice", 150));

        mockMvc.perform(post("/api/comptes/FR001/depot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\": 50.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(150.0));
    }

    @Test
    void devraitRetourner400QuandLeMontantEstNegatif() throws Exception {
        when(service.deposer(anyString(), anyDouble()))
                .thenThrow(new ValidationException("Le montant doit etre strictement positif"));

        mockMvc.perform(post("/api/comptes/FR001/depot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\": -10.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Le montant doit etre strictement positif"));
    }

    @Test
    void devraitRetirerDunCompte() throws Exception {
        when(service.retirer("FR001", 40.0)).thenReturn(compte("FR001", "Alice", 60));

        mockMvc.perform(post("/api/comptes/FR001/retrait")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\": 40.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(60.0));
    }

    @Test
    void devraitRetourner409QuandLeSoldeEstInsuffisant() throws Exception {
        when(service.retirer(anyString(), anyDouble()))
                .thenThrow(new SoldeInsuffisantException("Solde insuffisant sur le compte FR001"));

        mockMvc.perform(post("/api/comptes/FR001/retrait")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\": 1000.0}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Solde insuffisant sur le compte FR001"));
    }

    @Test
    void devraitEffectuerUnVirement() throws Exception {
        when(service.virer(eq("FR001"), eq("FR002"), anyDouble())).thenReturn(compte("FR001", "Alice", 120));

        mockMvc.perform(post("/api/comptes/virement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"source\": \"FR001\", \"destination\": \"FR002\", \"montant\": 80.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(120.0));
    }
}
