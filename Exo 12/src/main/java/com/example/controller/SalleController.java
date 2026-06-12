package com.example.controller;

import com.example.dto.SalleRequest;
import com.example.model.Salle;
import com.example.service.SalleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class SalleController {

    private final SalleService service;

    public SalleController(SalleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Salle> creer(@RequestBody SalleRequest requete) {
        Salle salle = service.creerSalle(requete.getNom(), requete.getCapacite());
        return ResponseEntity.status(HttpStatus.CREATED).body(salle);
    }

    @GetMapping
    public List<Salle> lister() {
        return service.listerSalles();
    }
}
