package com.example.service;

import com.example.exception.TicketIntrouvableException;
import com.example.exception.TransitionInterditeException;
import com.example.exception.ValidationException;
import com.example.model.Priorite;
import com.example.model.Statut;
import com.example.model.Ticket;
import com.example.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket creerTicket(String titre, Priorite priorite) {
        if (titre == null || titre.trim().length() < 3) {
            throw new ValidationException("Le titre doit contenir au moins 3 caracteres");
        }
        if (priorite == null) {
            throw new ValidationException("La priorite est obligatoire");
        }
        Ticket ticket = new Ticket();
        ticket.setTitre(titre);
        ticket.setPriorite(priorite);
        ticket.setStatut(Statut.OPEN);
        return repository.sauvegarder(ticket);
    }

    public Ticket trouverParId(Long id) {
        Ticket ticket = repository.trouverParId(id);
        if (ticket == null) {
            throw new TicketIntrouvableException("Le ticket " + id + " n'existe pas");
        }
        return ticket;
    }

    public List<Ticket> listerTickets() {
        return repository.tousLesTickets();
    }

    public Ticket changerStatut(Long id, Statut nouveauStatut) {
        Ticket ticket = trouverParId(id);
        if (!transitionAutorisee(ticket.getStatut(), nouveauStatut)) {
            throw new TransitionInterditeException(
                    "La transition de " + ticket.getStatut() + " vers " + nouveauStatut + " est interdite");
        }
        ticket.setStatut(nouveauStatut);
        return ticket;
    }

    private boolean transitionAutorisee(Statut actuel, Statut nouveau) {
        if (actuel == Statut.OPEN) {
            return nouveau == Statut.IN_PROGRESS || nouveau == Statut.RESOLVED;
        }
        if (actuel == Statut.IN_PROGRESS) {
            return nouveau == Statut.RESOLVED;
        }
        return false;
    }
}
