package com.example;

public class Commande {

    private final String emailClient;
    private final String referenceProduit;
    private final int quantite;
    private final ProfilClient profil;

    public Commande(String emailClient, String referenceProduit, int quantite, ProfilClient profil) {
        this.emailClient = emailClient;
        this.referenceProduit = referenceProduit;
        this.quantite = quantite;
        this.profil = profil;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public String getReferenceProduit() {
        return referenceProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public ProfilClient getProfil() {
        return profil;
    }
}
