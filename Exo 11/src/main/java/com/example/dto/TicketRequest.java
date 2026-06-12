package com.example.dto;

import com.example.model.Priorite;

public class TicketRequest {

    private String titre;
    private Priorite priorite;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }
}
