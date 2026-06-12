package com.example.repository;

import com.example.model.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation sauvegarder(Reservation reservation);

    Reservation trouverParId(Long id);

    List<Reservation> reservationsPourSalle(Long salleId);

    void viderTout();
}
