package com.example;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.fr.Soit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandeStepDefinitions {

    private final CatalogueProduit catalogue = mock(CatalogueProduit.class);
    private final ServiceCommande service = new ServiceCommande(catalogue);

    private RecuCommande recu;
    private CommandeRefuseeException refus;

    @Soit("un produit {string} nommé {string} au prix de {double} avec un stock de {int}")
    public void unProduit(String reference, String nom, double prix, int stock) {
        when(catalogue.trouverParReference(reference)).thenReturn(new Produit(reference, nom, prix, stock));
    }

    @Soit("aucun produit pour la référence {string}")
    public void aucunProduit(String reference) {
        when(catalogue.trouverParReference(reference)).thenReturn(null);
    }

    @Quand("le client {string} commande {int} unités de {string} avec le profil {string}")
    public void leClientCommande(String email, int quantite, String reference, String profil) {
        Commande commande = new Commande(email, reference, quantite, ProfilClient.valueOf(profil));
        try {
            recu = service.passerCommande(commande);
            refus = null;
        } catch (CommandeRefuseeException e) {
            refus = e;
            recu = null;
        }
    }

    @Alors("la commande est acceptée")
    public void laCommandeEstAcceptee() {
        assertNotNull(recu);
        assertNull(refus);
    }

    @Alors("la commande est refusée")
    public void laCommandeEstRefusee() {
        assertNotNull(refus);
        assertNull(recu);
    }

    @Et("le montant total est {double}")
    public void leMontantTotalEst(double montant) {
        assertEquals(montant, recu.getMontantTotal(), 0.001);
    }

    @Et("le reçu concerne la référence {string} avec la quantité {int}")
    public void leRecuConcerne(String reference, int quantite) {
        assertEquals(reference, recu.getReferenceProduit());
        assertEquals(quantite, recu.getQuantite());
    }

    @Et("le reçu contient un message de confirmation")
    public void leRecuContientUnMessage() {
        assertNotNull(recu.getMessage());
        assertTrue(recu.getMessage().contains("confirmée"));
    }

    @Et("le message de refus contient {string}")
    public void leMessageDeRefusContient(String extrait) {
        assertTrue(refus.getMessage().contains(extrait));
    }
}
