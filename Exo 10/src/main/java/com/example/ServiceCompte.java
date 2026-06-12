package com.example;

public class ServiceCompte {

    private final RepositoryUtilisateur repository;

    public ServiceCompte(RepositoryUtilisateur repository) {
        this.repository = repository;
    }

    public ConfirmationInscription inscrire(String email, String nomUtilisateur, String motDePasse) {
        if (repository.trouverParNom(nomUtilisateur) != null) {
            throw new CompteDejaExistantException("Le compte " + nomUtilisateur + " existe déjà");
        }
        Utilisateur utilisateur = new Utilisateur(email, nomUtilisateur, motDePasse);
        repository.sauvegarder(utilisateur);
        return new ConfirmationInscription("Compte créé pour " + nomUtilisateur);
    }
}
