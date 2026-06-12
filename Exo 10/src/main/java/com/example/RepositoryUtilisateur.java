package com.example;

public interface RepositoryUtilisateur {

    Utilisateur trouverParNom(String nomUtilisateur);

    void sauvegarder(Utilisateur utilisateur);
}
