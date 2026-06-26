package com.example.service;

import com.example.exception.CompteDejaExistantException;
import com.example.exception.CompteIntrouvableException;
import com.example.exception.SoldeInsuffisantException;
import com.example.exception.ValidationException;
import com.example.model.Compte;
import com.example.repository.CompteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompteService {

    private final CompteRepository repository;

    public CompteService(CompteRepository repository) {
        this.repository = repository;
    }

    public Compte creerCompte(String numero, String titulaire) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new ValidationException("Le numero de compte est obligatoire");
        }
        if (titulaire == null || titulaire.trim().isEmpty()) {
            throw new ValidationException("Le titulaire est obligatoire");
        }
        if (repository.existe(numero)) {
            throw new CompteDejaExistantException("Le compte " + numero + " existe deja");
        }
        Compte compte = new Compte();
        compte.setNumero(numero);
        compte.setTitulaire(titulaire);
        compte.setSolde(0);
        return repository.sauvegarder(compte);
    }

    public Compte consulter(String numero) {
        Compte compte = repository.trouverParNumero(numero);
        if (compte == null) {
            throw new CompteIntrouvableException("Le compte " + numero + " n'existe pas");
        }
        return compte;
    }

    public List<Compte> listerComptes() {
        return repository.tousLesComptes();
    }

    public Compte deposer(String numero, double montant) {
        Compte compte = consulter(numero);
        if (montant <= 0) {
            throw new ValidationException("Le montant doit etre strictement positif");
        }
        compte.setSolde(compte.getSolde() + montant);
        return repository.sauvegarder(compte);
    }

    public Compte retirer(String numero, double montant) {
        Compte compte = consulter(numero);
        if (montant <= 0) {
            throw new ValidationException("Le montant doit etre strictement positif");
        }
        if (compte.getSolde() < montant) {
            throw new SoldeInsuffisantException("Solde insuffisant sur le compte " + numero);
        }
        compte.setSolde(compte.getSolde() - montant);
        return repository.sauvegarder(compte);
    }

    public Compte virer(String source, String destination, double montant) {
        Compte compteSource = consulter(source);
        Compte compteDestination = consulter(destination);
        if (montant <= 0) {
            throw new ValidationException("Le montant doit etre strictement positif");
        }
        if (compteSource.getSolde() < montant) {
            throw new SoldeInsuffisantException("Solde insuffisant sur le compte " + source);
        }
        compteSource.setSolde(compteSource.getSolde() - montant);
        compteDestination.setSolde(compteDestination.getSolde() + montant);
        repository.sauvegarder(compteSource);
        repository.sauvegarder(compteDestination);
        return compteSource;
    }
}
