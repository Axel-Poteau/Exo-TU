package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompteApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void parcoursCompletDunVirementEntreDeuxComptes() throws Exception {
        mockMvc.perform(post("/api/comptes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": \"FR100\", \"titulaire\": \"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.solde").value(0.0));

        mockMvc.perform(post("/api/comptes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": \"FR200\", \"titulaire\": \"Bob\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/comptes/FR100/depot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\": 300.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(300.0));

        mockMvc.perform(post("/api/comptes/virement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"source\": \"FR100\", \"destination\": \"FR200\", \"montant\": 120.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(180.0));

        mockMvc.perform(get("/api/comptes/FR200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(120.0));
    }
}
