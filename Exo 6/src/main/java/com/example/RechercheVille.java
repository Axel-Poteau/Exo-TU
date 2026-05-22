package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RechercheVille {

    private List<String> villes = new ArrayList<>(Arrays.asList(
            "Paris", "Budapest", "Skopje", "Rotterdam", "Valence", "Vancouver",
            "Amsterdam", "Vienne", "Sydney", "New York", "Londres", "Bangkok",
            "Hong Kong", "Dubaï", "Rome", "Istanbul"
    ));

    public List<String> Rechercher(String mot) {
        if (mot.equals("*")) {
            return new ArrayList<>(villes);
        }
        if (mot.length() < 2) {
            throw new NotFoundException("Le texte de recherche doit contenir au moins 2 caractères");
        }
        String motLower = mot.toLowerCase();
        List<String> resultat = new ArrayList<>();
        for (String ville : villes) {
            if (ville.toLowerCase().contains(motLower)) {
                resultat.add(ville);
            }
        }
        return resultat;
    }
}