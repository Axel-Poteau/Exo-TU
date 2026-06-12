package com.example.repository;

import com.example.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TicketRepositoryEnMemoire implements TicketRepository {

    private final Map<Long, Ticket> tickets = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Ticket sauvegarder(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(sequence.getAndIncrement());
        }
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Ticket trouverParId(Long id) {
        return tickets.get(id);
    }

    @Override
    public List<Ticket> tousLesTickets() {
        return new ArrayList<>(tickets.values());
    }

    @Override
    public void viderTout() {
        tickets.clear();
    }
}
