package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FrameTest {

    private IGenerateur generateur;

    @BeforeEach
    void setUp() {
        generateur = mock(IGenerateur.class);
    }

    @Test
    void shouldIncreaseScoreWhenFirstRollIsMadeInStandardFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(4);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();

        assertEquals(4, frame.getScore());
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeInStandardFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(4, 3);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();
        frame.makeRoll();

        assertEquals(7, frame.getScore());
    }

    @Test
    void shouldRejectSecondRollWhenStandardFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10);
        Frame frame = new Frame(generateur, false);

        assertTrue(frame.makeRoll());
        assertFalse(frame.makeRoll());
    }

    @Test
    void shouldRejectThirdRollWhenStandardFrameAlreadyHasTwoRolls() {
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);
        Frame frame = new Frame(generateur, false);

        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
        assertFalse(frame.makeRoll());
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeAfterStrikeInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();

        assertEquals(15, frame.getScore());
    }

    @Test
    void shouldAcceptSecondRollWhenLastFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5);
        Frame frame = new Frame(generateur, true);

        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5, 3);
        Frame frame = new Frame(generateur, true);

        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterStrikeInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5, 3);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        assertEquals(18, frame.getScore());
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithSpare() {
        when(generateur.randomPin(anyInt())).thenReturn(6, 4, 7);
        Frame frame = new Frame(generateur, true);

        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterSpareInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(6, 4, 7);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        assertEquals(17, frame.getScore());
    }

    @Test
    void shouldRejectThirdRollWhenLastFrameHasNoStrikeOrSpare() {
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);
        Frame frame = new Frame(generateur, true);

        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
        assertFalse(frame.makeRoll());
    }

    @Test
    void shouldRejectFourthRollInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 10, 10);
        Frame frame = new Frame(generateur, true);

        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
        assertFalse(frame.makeRoll());
    }
}
