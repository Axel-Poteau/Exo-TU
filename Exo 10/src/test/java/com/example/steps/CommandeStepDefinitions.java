package com.example.steps;

import com.example.Commande;
import com.example.CommandeIntrouvableException;
import com.example.ConfirmationCommande;
import com.example.ProduitAbsentException;
import com.example.RepositoryCommande;
import com.example.ServiceCommande;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.fr.Soit;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandeStepDefinitions {

    private final RepositoryCommande repository = mock(RepositoryCommande.class);
    private final ServiceCommande service = new ServiceCommande(repository);

    private final Map<String, Commande> commandes = new HashMap<>();

    private String confirmationAjout;
    private ConfirmationCommande confirmationCommande;
    private Exception erreur;

    @Soit("une commande en cours numéro {string}")
    public void uneCommandeEnCours(String numero) {
        Commande commande = new Commande(numero);
        commandes.put(numero, commande);
        when(repository.trouverParNumero(numero)).thenReturn(commande);
    }

    @Soit("la commande {string} contient déjà {int} exemplaire(s) du produit {string}")
    public void laCommandeContientDeja(String numero, int quantite, String produit) {
        for (int i = 0; i < quantite; i++) {
            commandes.get(numero).ajouter(produit);
        }
    }

    @Quand("l'utilisateur ajoute le produit {string} à la commande {string}")
    public void ajouteLeProduit(String produit, String numero) {
        try {
            confirmationAjout = service.ajouterProduit(numero, produit);
            erreur = null;
        } catch (Exception e) {
            erreur = e;
            confirmationAjout = null;
        }
    }

    @Alors("l'ajout est confirmé")
    public void lAjoutEstConfirme() {
        assertNotNull(confirmationAjout);
        assertNull(erreur);
    }

    @Et("la commande {string} contient {int} exemplaire(s) du produit {string}")
    public void laCommandeContient(String numero, int quantite, String produit) {
        assertEquals(quantite, commandes.get(numero).getQuantite(produit));
    }

    @Quand("l'utilisateur supprime le produit {string} de la commande {string}")
    public void supprimeLeProduit(String produit, String numero) {
        try {
            service.supprimerProduit(numero, produit);
            erreur = null;
        } catch (Exception e) {
            erreur = e;
        }
    }

    @Alors("la commande {string} ne contient plus le produit {string}")
    public void laCommandeNeContientPlus(String numero, String produit) {
        assertFalse(commandes.get(numero).contient(produit));
    }

    @Quand("l'utilisateur valide la commande {string}")
    public void valideLaCommande(String numero) {
        try {
            confirmationCommande = service.valider(numero);
            erreur = null;
        } catch (Exception e) {
            erreur = e;
            confirmationCommande = null;
        }
    }

    @Alors("la commande est confirmée")
    public void laCommandeEstConfirmee() {
        assertNotNull(confirmationCommande);
        assertNull(erreur);
    }

    @Alors("une erreur de commande introuvable est renvoyée")
    public void uneErreurDeCommandeIntrouvable() {
        assertNotNull(erreur);
        assertTrue(erreur instanceof CommandeIntrouvableException);
    }

    @Alors("une erreur de produit absent est renvoyée")
    public void uneErreurDeProduitAbsent() {
        assertNotNull(erreur);
        assertTrue(erreur instanceof ProduitAbsentException);
    }
}
