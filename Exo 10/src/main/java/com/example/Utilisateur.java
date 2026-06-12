package com.example;

public class Utilisateur {

    private final String email;
    private final String nomUtilisateur;
    private final String motDePasse;

    public Utilisateur(String email, String nomUtilisateur, String motDePasse) {
        this.email = email;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
}
