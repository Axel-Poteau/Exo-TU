package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RechercheVilleTest {

    private RechercheVille recherche;

    @BeforeEach
    void setUp() {
        recherche = new RechercheVille();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSearchTextIsEmpty() {
        assertThrows(NotFoundException.class, () -> recherche.Rechercher(""));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSearchTextHasOneCharacter() {
        assertThrows(NotFoundException.class, () -> recherche.Rechercher("a"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", "P", "Z"})
    void shouldThrowNotFoundExceptionForSingleCharacterOrEmpty(String mot) {
        assertThrows(NotFoundException.class, () -> recherche.Rechercher(mot));
    }

    @Test
    void shouldReturnCitiesStartingWithVa() {
        List<String> result = recherche.Rechercher("Va");
        assertEquals(2, result.size());
        assertTrue(result.contains("Valence"));
        assertTrue(result.contains("Vancouver"));
    }

    @Test
    void shouldReturnSingleCityWhenPrefixMatchesOne() {
        List<String> result = recherche.Rechercher("Par");
        assertEquals(1, result.size());
        assertTrue(result.contains("Paris"));
    }

    @Test
    void shouldReturnEmptyListWhenNoCityMatches() {
        List<String> result = recherche.Rechercher("ZZ");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldBeCaseInsensitiveLowercase() {
        List<String> result = recherche.Rechercher("va");
        assertEquals(2, result.size());
        assertTrue(result.contains("Valence"));
        assertTrue(result.contains("Vancouver"));
    }

    @Test
    void shouldBeCaseInsensitiveUppercase() {
        List<String> result = recherche.Rechercher("PAR");
        assertEquals(1, result.size());
        assertTrue(result.contains("Paris"));
    }

    @Test
    void shouldBeCaseInsensitiveMixedCase() {
        List<String> result = recherche.Rechercher("vAlEnCe");
        assertEquals(1, result.size());
        assertTrue(result.contains("Valence"));
    }

    @Test
    void shouldFindCityByPartialMatchInMiddle() {
        List<String> result = recherche.Rechercher("ape");
        assertEquals(1, result.size());
        assertTrue(result.contains("Budapest"));
    }

    @Test
    void shouldFindCityByPartialMatchAtEnd() {
        List<String> result = recherche.Rechercher("dam");
        assertTrue(result.contains("Rotterdam"));
        assertTrue(result.contains("Amsterdam"));
    }

    @Test
    void shouldFindCityByPartialMatchCaseInsensitive() {
        List<String> result = recherche.Rechercher("ROM");
        assertTrue(result.contains("Rome"));
    }

    @Test
    void shouldReturnAllCitiesWhenSearchIsAsterisk() {
        List<String> result = recherche.Rechercher("*");
        assertEquals(16, result.size());
        assertTrue(result.contains("Paris"));
        assertTrue(result.contains("Budapest"));
        assertTrue(result.contains("Skopje"));
        assertTrue(result.contains("Rotterdam"));
        assertTrue(result.contains("Valence"));
        assertTrue(result.contains("Vancouver"));
        assertTrue(result.contains("Amsterdam"));
        assertTrue(result.contains("Vienne"));
        assertTrue(result.contains("Sydney"));
        assertTrue(result.contains("New York"));
        assertTrue(result.contains("Londres"));
        assertTrue(result.contains("Bangkok"));
        assertTrue(result.contains("Hong Kong"));
        assertTrue(result.contains("Dubaï"));
        assertTrue(result.contains("Rome"));
        assertTrue(result.contains("Istanbul"));
    }
}