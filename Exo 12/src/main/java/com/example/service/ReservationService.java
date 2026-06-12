package com.example.service;

import com.example.exception.ConflitReservationException;
import com.example.exception.RessourceIntrouvableException;
import com.example.exception.ValidationException;
import com.example.model.Reservation;
import com.example.model.StatutReservation;
import com.example.repository.ReservationRepository;
import com.example.repository.SalleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(SalleRepository salleRepository, ReservationRepository reservationRepository) {
        this.salleRepository = salleRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation creerReservation(Long salleId, String nomPersonne, LocalDateTime debut, LocalDateTime fin) {
        if (salleRepository.trouverParId(salleId) == null) {
            throw new RessourceIntrouvableException("La salle " + salleId + " n'existe pas");
        }
        if (nomPersonne == null || nomPersonne.isBlank()) {
            throw new ValidationException("Le nom de la personne est obligatoire");
        }
        if (!fin.isAfter(debut)) {
            throw new ValidationException("La date de fin doit etre apres la date de debut");
        }
        // seules les reservations confirmees bloquent le creneau
        for (Reservation existante : reservationRepository.reservationsPourSalle(salleId)) {
            if (existante.getStatut() == StatutReservation.CONFIRMEE && seChevauchent(existante, debut, fin)) {
                throw new ConflitReservationException("Le creneau chevauche une reservation existante");
            }
        }
        Reservation reservation = new Reservation();
        reservation.setSalleId(salleId);
        reservation.setNomPersonne(nomPersonne);
        reservation.setDebut(debut);
        reservation.setFin(fin);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        return reservationRepository.sauvegarder(reservation);
    }

    public Reservation trouverParId(Long id) {
        Reservation reservation = reservationRepository.trouverParId(id);
        if (reservation == null) {
            throw new RessourceIntrouvableException("La reservation " + id + " n'existe pas");
        }
        return reservation;
    }

    public Reservation annuler(Long id) {
        Reservation reservation = trouverParId(id);
        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new ConflitReservationException("La reservation est deja annulee");
        }
        reservation.setStatut(StatutReservation.ANNULEE);
        return reservation;
    }

    // deux creneaux se chevauchent si chacun commence avant la fin de l'autre
    private boolean seChevauchent(Reservation existante, LocalDateTime debut, LocalDateTime fin) {
        return debut.isBefore(existante.getFin()) && existante.getDebut().isBefore(fin);
    }
}
