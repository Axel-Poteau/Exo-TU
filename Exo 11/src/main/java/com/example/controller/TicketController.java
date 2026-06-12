package com.example.controller;

import com.example.dto.ChangementStatutRequest;
import com.example.dto.TicketRequest;
import com.example.model.Ticket;
import com.example.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Ticket> creer(@RequestBody TicketRequest requete) {
        Ticket ticket = service.creerTicket(requete.getTitre(), requete.getPriorite());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @GetMapping("/{id}")
    public Ticket consulter(@PathVariable Long id) {
        return service.trouverParId(id);
    }

    @GetMapping
    public List<Ticket> lister() {
        return service.listerTickets();
    }

    @PutMapping("/{id}/statut")
    public Ticket changerStatut(@PathVariable Long id, @RequestBody ChangementStatutRequest requete) {
        return service.changerStatut(id, requete.getStatut());
    }
}
