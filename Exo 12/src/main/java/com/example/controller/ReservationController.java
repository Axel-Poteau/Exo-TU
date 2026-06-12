package com.example.controller;

import com.example.dto.ReservationRequest;
import com.example.model.Reservation;
import com.example.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Reservation> creer(@RequestBody ReservationRequest requete) {
        Reservation reservation = service.creerReservation(requete.getSalleId(), requete.getNomPersonne(),
                requete.getDebut(), requete.getFin());
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/{id}")
    public Reservation consulter(@PathVariable Long id) {
        return service.trouverParId(id);
    }

    @PatchMapping("/{id}/cancel")
    public Reservation annuler(@PathVariable Long id) {
        return service.annuler(id);
    }
}
