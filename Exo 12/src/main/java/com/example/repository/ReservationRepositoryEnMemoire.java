package com.example.repository;

import com.example.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationRepositoryEnMemoire implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Reservation sauvegarder(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(sequence.getAndIncrement());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public Reservation trouverParId(Long id) {
        return reservations.get(id);
    }

    @Override
    public List<Reservation> reservationsPourSalle(Long salleId) {
        List<Reservation> resultat = new ArrayList<>();
        for (Reservation reservation : reservations.values()) {
            if (reservation.getSalleId().equals(salleId)) {
                resultat.add(reservation);
            }
        }
        return resultat;
    }

    @Override
    public void viderTout() {
        reservations.clear();
    }
}
