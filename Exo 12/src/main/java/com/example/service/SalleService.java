package com.example.service;

import com.example.exception.ValidationException;
import com.example.model.Salle;
import com.example.repository.SalleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalleService {

    private final SalleRepository repository;

    public SalleService(SalleRepository repository) {
        this.repository = repository;
    }

    public Salle creerSalle(String nom, int capacite) {
        if (nom == null || nom.isBlank()) {
            throw new ValidationException("Le nom de la salle est obligatoire");
        }
        if (capacite < 1) {
            throw new ValidationException("La capacite doit etre superieure ou egale a 1");
        }
        Salle salle = new Salle();
        salle.setNom(nom);
        salle.setCapacite(capacite);
        return repository.sauvegarder(salle);
    }

    public List<Salle> listerSalles() {
        return repository.toutesLesSalles();
    }
}
