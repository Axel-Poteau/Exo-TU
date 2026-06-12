package com.example.steps;

import com.example.CompteDejaExistantException;
import com.example.ConfirmationInscription;
import com.example.RepositoryUtilisateur;
import com.example.ResultatConnexion;
import com.example.ServiceCompte;
import com.example.Utilisateur;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.fr.Soit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CompteStepDefinitions {

    private final RepositoryUtilisateur repository = mock(RepositoryUtilisateur.class);
    private final ServiceCompte service = new ServiceCompte(repository);

    private ConfirmationInscription confirmation;
    private CompteDejaExistantException erreurInscription;
    private ResultatConnexion resultat;

    @Soit("aucun compte existant pour le nom d'utilisateur {string}")
    public void aucunCompteExistant(String nom) {
        when(repository.trouverParNom(nom)).thenReturn(null);
    }

    @Soit("un compte existant pour le nom d'utilisateur {string}")
    public void unCompteExistant(String nom) {
        when(repository.trouverParNom(nom)).thenReturn(new Utilisateur(nom + "@mail.com", nom, "secret123"));
    }

    @Soit("un compte {string} avec le mot de passe {string}")
    public void unCompteAvecMotDePasse(String nom, String motDePasse) {
        when(repository.trouverParNom(nom)).thenReturn(new Utilisateur(nom + "@mail.com", nom, motDePasse));
    }

    @Quand("l'utilisateur s'inscrit avec l'email {string}, le nom d'utilisateur {string} et le mot de passe {string}")
    public void sInscrit(String email, String nom, String motDePasse) {
        try {
            confirmation = service.inscrire(email, nom, motDePasse);
            erreurInscription = null;
        } catch (CompteDejaExistantException e) {
            erreurInscription = e;
            confirmation = null;
        }
    }

    @Alors("l'inscription est confirmée")
    public void lInscriptionEstConfirmee() {
        assertNotNull(confirmation);
        assertNull(erreurInscription);
    }

    @Et("le compte est enregistré")
    public void leCompteEstEnregistre() {
        verify(repository).sauvegarder(any(Utilisateur.class));
    }

    @Alors("l'inscription est refusée")
    public void lInscriptionEstRefusee() {
        assertNotNull(erreurInscription);
        assertNull(confirmation);
    }

    @Et("le message d'erreur d'inscription contient {string}")
    public void leMessageDerreurDinscriptionContient(String extrait) {
        assertTrue(erreurInscription.getMessage().contains(extrait));
    }

    @Et("aucun compte n'est enregistré")
    public void aucunCompteNestEnregistre() {
        verify(repository, never()).sauvegarder(any(Utilisateur.class));
    }

    @Quand("l'utilisateur se connecte avec le nom {string} et le mot de passe {string}")
    public void seConnecte(String nom, String motDePasse) {
        resultat = service.connecter(nom, motDePasse);
    }

    @Alors("la connexion est acceptée")
    public void laConnexionEstAcceptee() {
        assertTrue(resultat.estReussie());
    }

    @Et("l'utilisateur est redirigé vers la page {string}")
    public void estRedirigeVersLaPage(String page) {
        assertEquals(page, resultat.getPageRedirection());
    }

    @Alors("la connexion est refusée")
    public void laConnexionEstRefusee() {
        assertFalse(resultat.estReussie());
    }

    @Et("le message d'erreur de connexion contient {string}")
    public void leMessageDerreurDeConnexionContient(String extrait) {
        assertTrue(resultat.getMessageErreur().contains(extrait));
    }
}
