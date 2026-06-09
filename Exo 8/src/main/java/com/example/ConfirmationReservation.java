package com.example;

public class ConfirmationReservation {

    private final String codeSalle;
    private final String emailUtilisateur;
    private final String message;

    public ConfirmationReservation(String codeSalle, String emailUtilisateur, String message) {
        this.codeSalle = codeSalle;
        this.emailUtilisateur = emailUtilisateur;
        this.message = message;
    }

    public String getCodeSalle() {
        return codeSalle;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public String getMessage() {
        return message;
    }
}
