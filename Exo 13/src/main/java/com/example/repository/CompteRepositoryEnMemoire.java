package com.example.repository;

import com.example.model.Compte;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CompteRepositoryEnMemoire implements CompteRepository {

    private final Map<String, Compte> comptes = new HashMap<>();

    @Override
    public Compte sauvegarder(Compte compte) {
        comptes.put(compte.getNumero(), compte);
        return compte;
    }

    @Override
    public Compte trouverParNumero(String numero) {
        return comptes.get(numero);
    }

    @Override
    public boolean existe(String numero) {
        return comptes.containsKey(numero);
    }

    @Override
    public List<Compte> tousLesComptes() {
        return new ArrayList<>(comptes.values());
    }

    @Override
    public void viderTout() {
        comptes.clear();
    }
}
