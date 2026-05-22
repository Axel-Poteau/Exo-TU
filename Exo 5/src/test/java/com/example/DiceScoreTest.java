package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiceScoreTest {

    @Mock
    private Ide de;

    @Test
    void shouldReturnValueTimesTwoPlusTenWhenBothDiceAreEqual() {
        // Arrange
        when(de.getRoll()).thenReturn(3, 3);
        DiceScore diceScore = new DiceScore(de);

        // Act
        int score = diceScore.getScore();

        // Assert
        assertEquals(16, score); // 3 * 2 + 10
    }

    @Test
    void shouldReturnThirtyWhenBothDiceAreSix() {
        // Arrange
        when(de.getRoll()).thenReturn(6, 6);
        DiceScore diceScore = new DiceScore(de);

        // Act
        int score = diceScore.getScore();

        // Assert
        assertEquals(30, score);
    }

    @Test
    void shouldReturnHighestValueWhenDiceAreDifferent() {
        // Arrange
        when(de.getRoll()).thenReturn(2, 5);
        DiceScore diceScore = new DiceScore(de);

        // Act
        int score = diceScore.getScore();

        // Assert
        assertEquals(5, score);
    }

    @Test
    void shouldReturnHighestValueWhenFirstDieIsHigher() {
        // Arrange
        when(de.getRoll()).thenReturn(4, 1);
        DiceScore diceScore = new DiceScore(de);

        // Act
        int score = diceScore.getScore();

        // Assert
        assertEquals(4, score);
    }

    @Test
    void shouldReturnTwelveWhenBothDiceAreOne() {
        // Arrange
        when(de.getRoll()).thenReturn(1, 1);
        DiceScore diceScore = new DiceScore(de);

        // Act
        int score = diceScore.getScore();

        // Assert
        assertEquals(12, score); // 1 * 2 + 10
    }
}