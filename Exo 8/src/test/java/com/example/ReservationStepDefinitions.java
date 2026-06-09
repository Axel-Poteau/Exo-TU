package com.example;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.fr.Soit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservationStepDefinitions {

    private final CatalogueSalle catalogue = mock(CatalogueSalle.class);
    private final RegistreReservations registre = mock(RegistreReservations.class);
    private final ServiceNotification notification = mock(ServiceNotification.class);
    private final ServiceReservation service = new ServiceReservation(catalogue, registre, notification);

    private final List<Reservation> existantes = new ArrayList<>();

    private ConfirmationReservation confirmation;
    private ReservationRefuseeException refus;

    @Soit("une salle {string} nommée {string} avec une capacité de {int}")
    public void uneSalle(String code, String nom, int capacite) {
        when(catalogue.trouverParCode(code)).thenReturn(new Salle(code, nom, capacite));
        when(registre.reservationsPour(code)).thenReturn(existantes);
    }

    @Soit("aucune salle pour le code {string}")
    public void aucuneSalle(String code) {
        when(catalogue.trouverParCode(code)).thenReturn(null);
    }

    @Soit("une réservation existante sur la salle {string} du {string} au {string}")
    public void uneReservationExistante(String code, String debut, String fin) {
        existantes.add(new Reservation("existant@mail.com", code, 1,
                LocalDateTime.parse(debut), LocalDateTime.parse(fin)));
        when(registre.reservationsPour(code)).thenReturn(existantes);
    }

    @Quand("{string} réserve la salle {string} pour {int} participants du {string} au {string}")
    public void reserve(String email, String code, int participants, String debut, String fin) {
        Reservation reservation = new Reservation(email, code, participants,
                LocalDateTime.parse(debut), LocalDateTime.parse(fin));
        try {
            confirmation = service.reserver(reservation);
            refus = null;
        } catch (ReservationRefuseeException e) {
            refus = e;
            confirmation = null;
        }
    }

    @Alors("la réservation est acceptée")
    public void laReservationEstAcceptee() {
        assertNotNull(confirmation);
        assertNull(refus);
    }

    @Alors("la réservation est refusée")
    public void laReservationEstRefusee() {
        assertNotNull(refus);
        assertNull(confirmation);
    }

    @Et("une confirmation est envoyée")
    public void uneConfirmationEstEnvoyee() {
        verify(notification, times(1)).envoyerConfirmation(org.mockito.ArgumentMatchers.any());
    }

    @Et("aucune confirmation n'est envoyée")
    public void aucuneConfirmationNestEnvoyee() {
        verify(notification, never()).envoyerConfirmation(org.mockito.ArgumentMatchers.any());
    }

    @Et("le message de refus contient {string}")
    public void leMessageDeRefusContient(String extrait) {
        assertTrue(refus.getMessage().contains(extrait));
    }
}
