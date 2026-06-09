package com.example;

public enum ProfilClient {

    STANDARD(0.0),
    PREMIUM(0.10),
    VIP(0.20);

    private final double remise;

    ProfilClient(double remise) {
        this.remise = remise;
    }

    public double getRemise() {
        return remise;
    }
}
