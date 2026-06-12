package com.example.service;

import com.example.exception.ValidationException;
import com.example.model.Salle;
import com.example.repository.SalleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalleServiceTest {

    @Mock
    private SalleRepository repository;

    @InjectMocks
    private SalleService service;

    @Test
    void devraitCreerUneSalle() {
        when(repository.sauvegarder(any(Salle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Salle salle = service.creerSalle("Salle A", 8);

        assertEquals("Salle A", salle.getNom());
        assertEquals(8, salle.getCapacite());
        verify(repository).sauvegarder(any(Salle.class));
    }

    @Test
    void devraitRefuserUnNomManquant() {
        assertThrows(ValidationException.class, () -> service.creerSalle(null, 8));
        verify(repository, never()).sauvegarder(any(Salle.class));
    }

    @Test
    void devraitRefuserUneCapaciteInvalide() {
        assertThrows(ValidationException.class, () -> service.creerSalle("Salle A", 0));
        verify(repository, never()).sauvegarder(any(Salle.class));
    }
}