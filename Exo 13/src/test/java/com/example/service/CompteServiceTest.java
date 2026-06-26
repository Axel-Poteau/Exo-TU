package com.example.service;

import com.example.exception.CompteDejaExistantException;
import com.example.exception.CompteIntrouvableException;
import com.example.exception.SoldeInsuffisantException;
import com.example.exception.ValidationException;
import com.example.model.Compte;
import com.example.repository.CompteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompteServiceTest {

    @Mock
    private CompteRepository repository;

    @InjectMocks
    private CompteService service;

    private Compte compte(String numero, String titulaire, double solde) {
        Compte compte = new Compte();
        compte.setNumero(numero);
        compte.setTitulaire(titulaire);
        compte.setSolde(solde);
        return compte;
    }

    @Test
    void devraitCreerUnCompteAvecUnSoldeDeZero() {
        // Arrange
        when(repository.existe("FR001")).thenReturn(false);
        when(repository.sauvegarder(any(Compte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Compte resultat = service.creerCompte("FR001", "Alice");

        // Assert
        assertEquals("FR001", resultat.getNumero());
        assertEquals("Alice", resultat.getTitulaire());
        assertEquals(0, resultat.getSolde());
    }

    @Test
    void devraitRefuserUnNumeroDejaExistant() {
        // Arrange
        when(repository.existe("FR001")).thenReturn(true);

        // Act + Assert
        assertThrows(CompteDejaExistantException.class, () -> service.creerCompte("FR001", "Alice"));
        verify(repository, never()).sauvegarder(any(Compte.class));
    }

    @Test
    void devraitRefuserUnNumeroVide() {
        // Act + Assert
        assertThrows(ValidationException.class, () -> service.creerCompte("  ", "Alice"));
    }

    @Test
    void devraitRefuserUnTitulaireVide() {
        // Act + Assert
        assertThrows(ValidationException.class, () -> service.creerCompte("FR001", " "));
    }

    @Test
    void devraitConsulterUnCompteExistant() {
        // Arrange
        Compte existant = compte("FR001", "Alice", 100);
        when(repository.trouverParNumero("FR001")).thenReturn(existant);

        // Act
        Compte resultat = service.consulter("FR001");

        // Assert
        assertEquals(existant, resultat);
    }

    @Test
    void devraitEchouerSiLeCompteEstIntrouvable() {
        // Arrange
        when(repository.trouverParNumero("FR404")).thenReturn(null);

        // Act + Assert
        assertThrows(CompteIntrouvableException.class, () -> service.consulter("FR404"));
    }

    @Test
    void devraitListerTousLesComptes() {
        // Arrange
        when(repository.tousLesComptes()).thenReturn(List.of(compte("FR001", "Alice", 0), compte("FR002", "Bob", 50)));

        // Act
        List<Compte> comptes = service.listerComptes();

        // Assert
        assertEquals(2, comptes.size());
    }

    @Test
    void devraitDeposerUnMontantValide() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 100));
        when(repository.sauvegarder(any(Compte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Compte resultat = service.deposer("FR001", 50);

        // Assert
        assertEquals(150, resultat.getSolde());
    }

    @Test
    void devraitRefuserUnDepotNul() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 100));

        // Act + Assert
        assertThrows(ValidationException.class, () -> service.deposer("FR001", 0));
        verify(repository, never()).sauvegarder(any(Compte.class));
    }

    @Test
    void devraitRefuserUnDepotNegatif() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 100));

        // Act + Assert
        assertThrows(ValidationException.class, () -> service.deposer("FR001", -10));
    }

    @Test
    void devraitRetirerUnMontantValide() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 100));
        when(repository.sauvegarder(any(Compte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Compte resultat = service.retirer("FR001", 40);

        // Assert
        assertEquals(60, resultat.getSolde());
    }

    @Test
    void devraitRefuserUnRetraitNul() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 100));

        // Act + Assert
        assertThrows(ValidationException.class, () -> service.retirer("FR001", 0));
    }

    @Test
    void devraitRefuserUnRetraitNegatif() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 100));

        // Act + Assert
        assertThrows(ValidationException.class, () -> service.retirer("FR001", -5));
    }

    @Test
    void devraitRefuserUnRetraitAvecFondsInsuffisants() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 30));

        // Act + Assert
        assertThrows(SoldeInsuffisantException.class, () -> service.retirer("FR001", 100));
        verify(repository, never()).sauvegarder(any(Compte.class));
    }

    @Test
    void devraitEffectuerUnVirementValide() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 200));
        when(repository.trouverParNumero("FR002")).thenReturn(compte("FR002", "Bob", 0));
        when(repository.sauvegarder(any(Compte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Compte source = service.virer("FR001", "FR002", 80);

        // Assert
        assertEquals(120, source.getSolde());
    }

    @Test
    void devraitRefuserUnVirementNul() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 200));
        when(repository.trouverParNumero("FR002")).thenReturn(compte("FR002", "Bob", 0));

        // Act + Assert
        assertThrows(ValidationException.class, () -> service.virer("FR001", "FR002", 0));
        verify(repository, never()).sauvegarder(any(Compte.class));
    }

    @Test
    void devraitRefuserUnVirementNegatif() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 200));
        when(repository.trouverParNumero("FR002")).thenReturn(compte("FR002", "Bob", 0));

        // Act + Assert
        assertThrows(ValidationException.class, () -> service.virer("FR001", "FR002", -50));
    }

    @Test
    void devraitRefuserUnVirementAvecFondsInsuffisants() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 30));
        when(repository.trouverParNumero("FR002")).thenReturn(compte("FR002", "Bob", 0));

        // Act + Assert
        assertThrows(SoldeInsuffisantException.class, () -> service.virer("FR001", "FR002", 100));
        verify(repository, never()).sauvegarder(any(Compte.class));
    }

    @Test
    void devraitRefuserUnVirementVersUnCompteInexistant() {
        // Arrange
        when(repository.trouverParNumero("FR001")).thenReturn(compte("FR001", "Alice", 200));
        when(repository.trouverParNumero("FR999")).thenReturn(null);

        // Act + Assert
        assertThrows(CompteIntrouvableException.class, () -> service.virer("FR001", "FR999", 50));
    }

    @Test
    void devraitRefuserUnVirementDepuisUnCompteInexistant() {
        // Arrange
        when(repository.trouverParNumero("FR999")).thenReturn(null);

        // Act + Assert
        assertThrows(CompteIntrouvableException.class, () -> service.virer("FR999", "FR002", 50));
    }
}
