package com.example.service;

import com.example.exception.TicketIntrouvableException;
import com.example.exception.TransitionInterditeException;
import com.example.exception.ValidationException;
import com.example.model.Priorite;
import com.example.model.Statut;
import com.example.model.Ticket;
import com.example.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    private Ticket ticketAuStatut(Statut statut) {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitre("Imprimante en panne");
        ticket.setPriorite(Priorite.MEDIUM);
        ticket.setStatut(statut);
        return ticket;
    }

    @Test
    void devraitCreerUnTicketAvecLeStatutOpen() {
        // Arrange
        when(repository.sauvegarder(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket ticket = service.creerTicket("Ecran bleu au demarrage", Priorite.HIGH);

        // Assert
        assertEquals(Statut.OPEN, ticket.getStatut());
        assertEquals("Ecran bleu au demarrage", ticket.getTitre());
        assertEquals(Priorite.HIGH, ticket.getPriorite());
        verify(repository).sauvegarder(any(Ticket.class));
    }

    @Test
    void devraitRefuserUnTitreTropCourt() {
        // Act + Assert
        assertThrows(ValidationException.class, () -> service.creerTicket("ab", Priorite.LOW));
        verify(repository, never()).sauvegarder(any(Ticket.class));
    }

    @Test
    void devraitRefuserUnTitreComposeDespaces() {
        // Act + Assert
        assertThrows(ValidationException.class, () -> service.creerTicket("  a  ", Priorite.LOW));
        verify(repository, never()).sauvegarder(any(Ticket.class));
    }

    @Test
    void devraitRefuserUnTitreManquant() {
        // Act + Assert
        assertThrows(ValidationException.class, () -> service.creerTicket(null, Priorite.LOW));
    }

    @Test
    void devraitRefuserUnePrioriteManquante() {
        // Act + Assert
        assertThrows(ValidationException.class, () -> service.creerTicket("Mot de passe oublie", null));
    }

    @Test
    void devraitTrouverUnTicketExistant() {
        // Arrange
        Ticket existant = ticketAuStatut(Statut.OPEN);
        when(repository.trouverParId(1L)).thenReturn(existant);

        // Act
        Ticket ticket = service.trouverParId(1L);

        // Assert
        assertEquals(existant, ticket);
    }

    @Test
    void devraitEchouerQuandLeTicketNexistePas() {
        // Arrange
        when(repository.trouverParId(42L)).thenReturn(null);

        // Act + Assert
        assertThrows(TicketIntrouvableException.class, () -> service.trouverParId(42L));
    }

    @Test
    void devraitListerLesTickets() {
        // Arrange
        when(repository.tousLesTickets()).thenReturn(List.of(ticketAuStatut(Statut.OPEN), ticketAuStatut(Statut.RESOLVED)));

        // Act
        List<Ticket> tickets = service.listerTickets();

        // Assert
        assertEquals(2, tickets.size());
    }

    @Test
    void devraitAutoriserLaTransitionOpenVersInProgress() {
        // Arrange
        when(repository.trouverParId(1L)).thenReturn(ticketAuStatut(Statut.OPEN));

        // Act
        Ticket ticket = service.changerStatut(1L, Statut.IN_PROGRESS);

        // Assert
        assertEquals(Statut.IN_PROGRESS, ticket.getStatut());
    }

    @Test
    void devraitAutoriserLaTransitionOpenVersResolved() {
        // Arrange
        when(repository.trouverParId(1L)).thenReturn(ticketAuStatut(Statut.OPEN));

        // Act
        Ticket ticket = service.changerStatut(1L, Statut.RESOLVED);

        // Assert
        assertEquals(Statut.RESOLVED, ticket.getStatut());
    }

    @Test
    void devraitAutoriserLaTransitionInProgressVersResolved() {
        // Arrange
        when(repository.trouverParId(1L)).thenReturn(ticketAuStatut(Statut.IN_PROGRESS));

        // Act
        Ticket ticket = service.changerStatut(1L, Statut.RESOLVED);

        // Assert
        assertEquals(Statut.RESOLVED, ticket.getStatut());
    }

    @Test
    void devraitRefuserLeChangementDeStatutDunTicketResolu() {
        // Arrange
        when(repository.trouverParId(1L)).thenReturn(ticketAuStatut(Statut.RESOLVED));

        // Act + Assert
        assertThrows(TransitionInterditeException.class, () -> service.changerStatut(1L, Statut.IN_PROGRESS));
    }

    @Test
    void devraitRefuserUneTransitionInterdite() {
        // Arrange
        when(repository.trouverParId(1L)).thenReturn(ticketAuStatut(Statut.IN_PROGRESS));

        // Act + Assert
        assertThrows(TransitionInterditeException.class, () -> service.changerStatut(1L, Statut.OPEN));
    }
}