package com.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FibTest {

    @Test
    void shouldReturnNonEmptyListWhenRangeIs1() {
        // Arrange
        Fib fib = new Fib(1);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnListContainingZeroWhenRangeIs1() {
        // Arrange
        Fib fib = new Fib(1);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(List.of(0), result);
    }

    @Test
    void shouldContainThreeWhenRangeIs6() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertTrue(result.contains(3));
    }

    @Test
    void shouldContainSixElementsWhenRangeIs6() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(6, result.size());
    }

    @Test
    void shouldNotContainFourWhenRangeIs6() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertFalse(result.contains(4));
    }

    @Test
    void shouldMatchExpectedSeriesWhenRangeIs6() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(List.of(0, 1, 1, 2, 3, 5), result);
    }

    @Test
    void shouldBeSortedAscendingWhenRangeIs6() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i - 1) <= result.get(i),
                    "La liste doit être triée par ordre croissant");
        }
    }
}
