package com.example;

public class ServiceCommande {

    private final CatalogueProduit catalogue;

    public ServiceCommande(CatalogueProduit catalogue) {
        this.catalogue = catalogue;
    }

    public RecuCommande passerCommande(Commande commande) {
        Produit produit = catalogue.trouverParReference(commande.getReferenceProduit());
        if (produit == null) {
            throw new CommandeRefuseeException("Produit inconnu : " + commande.getReferenceProduit());
        }
        if (commande.getQuantite() > produit.getStock()) {
            throw new CommandeRefuseeException("Stock insuffisant pour le produit : " + produit.getReference());
        }
        double montantBrut = produit.getPrixUnitaire() * commande.getQuantite();
        double montantTotal = montantBrut * (1 - commande.getProfil().getRemise());
        return new RecuCommande(
                produit.getReference(),
                commande.getQuantite(),
                montantTotal,
                "Commande confirmée pour " + commande.getEmailClient());
    }
}
