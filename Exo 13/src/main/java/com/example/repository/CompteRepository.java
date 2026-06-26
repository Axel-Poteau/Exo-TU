package com.example.repository;

import com.example.model.Compte;

import java.util.List;

public interface CompteRepository {

    Compte sauvegarder(Compte compte);

    Compte trouverParNumero(String numero);

    boolean existe(String numero);

    List<Compte> tousLesComptes();

    void viderTout();
}
