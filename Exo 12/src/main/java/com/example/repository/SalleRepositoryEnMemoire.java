package com.example.repository;

import com.example.model.Salle;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

// Stockage en memoire, pas de base de donnees pour ce TP
@Repository
public class SalleRepositoryEnMemoire implements SalleRepository {

    private final Map<Long, Salle> salles = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Salle sauvegarder(Salle salle) {
        if (salle.getId() == null) {
            salle.setId(sequence.getAndIncrement());
        }
        salles.put(salle.getId(), salle);
        return salle;
    }

    @Override
    public Salle trouverParId(Long id) {
        return salles.get(id);
    }

    @Override
    public List<Salle> toutesLesSalles() {
        return new ArrayList<>(salles.values());
    }

    @Override
    public void viderTout() {
        salles.clear();
    }
}
