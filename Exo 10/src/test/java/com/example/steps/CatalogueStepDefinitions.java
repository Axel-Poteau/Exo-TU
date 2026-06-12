package com.example.steps;

import com.example.Produit;
import com.example.RepositoryProduit;
import com.example.ServiceCatalogue;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.fr.Soit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CatalogueStepDefinitions {

    private final RepositoryProduit repository = mock(RepositoryProduit.class);
    private final ServiceCatalogue service = new ServiceCatalogue(repository);

    private List<Produit> resultats;

    @Soit("un catalogue avec les produits suivants:")
    public void unCatalogueAvecLesProduits(DataTable table) {
        List<Produit> produits = new ArrayList<>();
        for (Map<String, String> ligne : table.asMaps()) {
            produits.add(new Produit(ligne.get("nom"), ligne.get("categorie"),
                    Double.parseDouble(ligne.get("prix"))));
        }
        when(repository.tousLesProduits()).thenReturn(produits);
    }

    @Quand("l'utilisateur recherche {string}")
    public void recherche(String motCle) {
        resultats = service.rechercher(motCle);
    }

    @Quand("l'utilisateur recherche les produits à moins de {int} euros")
    public void rechercheParPrixMax(int prixMax) {
        resultats = service.rechercherParPrixMax(prixMax);
    }

    @Quand("l'utilisateur sélectionne la catégorie {string}")
    public void selectionneLaCategorie(String categorie) {
        resultats = service.produitsParCategorie(categorie);
    }

    @Alors("les résultats contiennent {string}")
    public void lesResultatsContiennent(String nom) {
        assertTrue(resultats.stream().anyMatch(p -> p.getNom().equals(nom)));
    }

    @Et("les résultats ne contiennent pas {string}")
    public void lesResultatsNeContiennentPas(String nom) {
        assertTrue(resultats.stream().noneMatch(p -> p.getNom().equals(nom)));
    }

    @Alors("la liste de résultats est vide")
    public void laListeDeResultatsEstVide() {
        assertTrue(resultats.isEmpty());
    }
}
