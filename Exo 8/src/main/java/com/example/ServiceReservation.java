package com.example;

import java.util.List;

public class ServiceReservation {

    private final CatalogueSalle catalogue;
    private final RegistreReservations registre;
    private final ServiceNotification notification;

    public ServiceReservation(CatalogueSalle catalogue, RegistreReservations registre,
                              ServiceNotification notification) {
        this.catalogue = catalogue;
        this.registre = registre;
        this.notification = notification;
    }

    public ConfirmationReservation reserver(Reservation reservation) {
        Salle salle = catalogue.trouverParCode(reservation.getCodeSalle());
        if (salle == null) {
            throw new ReservationRefuseeException("Salle inconnue : " + reservation.getCodeSalle());
        }
        if (reservation.getNombreParticipants() > salle.getCapaciteMaximale()) {
            throw new ReservationRefuseeException("Capacité insuffisante pour la salle : " + salle.getCode());
        }
        if (!reservation.getDateFin().isAfter(reservation.getDateDebut())) {
            throw new ReservationRefuseeException("Période invalide : la date de fin doit être après la date de début");
        }
        List<Reservation> existantes = registre.reservationsPour(reservation.getCodeSalle());
        for (Reservation existante : existantes) {
            if (chevauche(reservation, existante)) {
                throw new ReservationRefuseeException("Conflit de réservation sur la salle : " + salle.getCode());
            }
        }
        notification.envoyerConfirmation(reservation);
        return new ConfirmationReservation(
                salle.getCode(),
                reservation.getEmailUtilisateur(),
                "Réservation confirmée pour " + reservation.getEmailUtilisateur());
    }

    private boolean chevauche(Reservation demandee, Reservation existante) {
        return demandee.getDateDebut().isBefore(existante.getDateFin())
                && existante.getDateDebut().isBefore(demandee.getDateFin());
    }
}
