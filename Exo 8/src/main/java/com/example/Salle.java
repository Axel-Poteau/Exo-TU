package com.example;

public class Salle {

    private final String code;
    private final String nom;
    private final int capaciteMaximale;

    public Salle(String code, String nom, int capaciteMaximale) {
        this.code = code;
        this.nom = nom;
        this.capaciteMaximale = capaciteMaximale;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public int getCapaciteMaximale() {
        return capaciteMaximale;
    }
}
