package com.example.service;

import com.example.exception.ConflitReservationException;
import com.example.exception.RessourceIntrouvableException;
import com.example.exception.ValidationException;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.StatutReservation;
import com.example.repository.ReservationRepository;
import com.example.repository.SalleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private SalleRepository salleRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService service;

    private final LocalDateTime debut = LocalDateTime.of(2026, 6, 15, 10, 0);
    private final LocalDateTime fin = LocalDateTime.of(2026, 6, 15, 11, 0);

    private Salle uneSalle() {
        Salle salle = new Salle();
        salle.setId(1L);
        salle.setNom("Salle A");
        salle.setCapacite(8);
        return salle;
    }

    private Reservation uneReservation(StatutReservation statut, LocalDateTime debut, LocalDateTime fin) {
        Reservation reservation = new Reservation();
        reservation.setId(5L);
        reservation.setSalleId(1L);
        reservation.setNomPersonne("Marie");
        reservation.setDebut(debut);
        reservation.setFin(fin);
        reservation.setStatut(statut);
        return reservation;
    }

    @Test
    void devraitCreerUneReservationValide() {
        when(salleRepository.trouverParId(1L)).thenReturn(uneSalle());
        when(reservationRepository.reservationsPourSalle(1L)).thenReturn(List.of());
        when(reservationRepository.sauvegarder(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reservation = service.creerReservation(1L, "Axel", debut, fin);

        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
        assertEquals("Axel", reservation.getNomPersonne());
        verify(reservationRepository).sauvegarder(any(Reservation.class));
    }

    @Test
    void devraitRefuserSiLaSalleNexistePas() {
        when(salleRepository.trouverParId(99L)).thenReturn(null);

        assertThrows(RessourceIntrouvableException.class,
                () -> service.creerReservation(99L, "Axel", debut, fin));
        verify(reservationRepository, never()).sauvegarder(any(Reservation.class));
    }

    @Test
    void devraitRefuserUnNomManquant() {
        when(salleRepository.trouverParId(1L)).thenReturn(uneSalle());

        assertThrows(ValidationException.class,
                () -> service.creerReservation(1L, null, debut, fin));
    }

    @Test
    void devraitRefuserUnCreneauInvalide() {
        when(salleRepository.trouverParId(1L)).thenReturn(uneSalle());

        assertThrows(ValidationException.class,
                () -> service.creerReservation(1L, "Axel", fin, debut));
    }

    @Test
    void devraitRefuserUnCreneauQuiChevaucheUneReservationConfirmee() {
        when(salleRepository.trouverParId(1L)).thenReturn(uneSalle());
        when(reservationRepository.reservationsPourSalle(1L))
                .thenReturn(List.of(uneReservation(StatutReservation.CONFIRMEE, debut, fin)));

        assertThrows(ConflitReservationException.class,
                () -> service.creerReservation(1L, "Axel", debut.plusMinutes(30), fin.plusMinutes(30)));
        verify(reservationRepository, never()).sauvegarder(any(Reservation.class));
    }

    @Test
    void devraitIgnorerLesReservationsAnnuleesPourLeChevauchement() {
        when(salleRepository.trouverParId(1L)).thenReturn(uneSalle());
        when(reservationRepository.reservationsPourSalle(1L))
                .thenReturn(List.of(uneReservation(StatutReservation.ANNULEE, debut, fin)));
        when(reservationRepository.sauvegarder(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reservation = service.creerReservation(1L, "Axel", debut.plusMinutes(30), fin.plusMinutes(30));

        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
    }

    @Test
    void devraitAnnulerUneReservationConfirmee() {
        when(reservationRepository.trouverParId(5L)).thenReturn(uneReservation(StatutReservation.CONFIRMEE, debut, fin));

        Reservation reservation = service.annuler(5L);

        assertEquals(StatutReservation.ANNULEE, reservation.getStatut());
    }

    @Test
    void devraitRefuserDannulerUneReservationDejaAnnulee() {
        when(reservationRepository.trouverParId(5L)).thenReturn(uneReservation(StatutReservation.ANNULEE, debut, fin));

        assertThrows(ConflitReservationException.class, () -> service.annuler(5L));
    }
}