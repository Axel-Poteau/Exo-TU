package com.example.repository;

import com.example.model.Salle;

import java.util.List;

public interface SalleRepository {

    Salle sauvegarder(Salle salle);

    Salle trouverParId(Long id);

    List<Salle> toutesLesSalles();

    void viderTout();
}
