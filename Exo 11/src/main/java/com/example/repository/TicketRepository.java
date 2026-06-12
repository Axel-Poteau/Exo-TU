package com.example.repository;

import com.example.model.Ticket;

import java.util.List;

public interface TicketRepository {

    Ticket sauvegarder(Ticket ticket);

    Ticket trouverParId(Long id);

    List<Ticket> tousLesTickets();

    void viderTout();
}
