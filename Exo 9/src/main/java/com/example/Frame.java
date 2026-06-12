package com.example;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    private int score;
    private boolean lastFrame;
    private IGenerateur generateur;
    private List<Roll> rolls;
    private int remainingPins;

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
        this.rolls = new ArrayList<>();
        this.remainingPins = 10;
    }

    public boolean makeRoll() {
        if (!canRoll()) {
            return false;
        }
        int pins = generateur.randomPin(remainingPins);
        rolls.add(new Roll(pins));
        score += pins;
        remainingPins -= pins;
        if (remainingPins == 0) {
            remainingPins = 10;
        }
        return true;
    }

    public int getScore() {
        return score;
    }

    public List<Roll> getRolls() {
        return rolls;
    }

    private boolean canRoll() {
        if (lastFrame) {
            if (rolls.size() < 2) {
                return true;
            }
            if (rolls.size() == 2) {
                return isStrike() || isSpare();
            }
            return false;
        }
        if (rolls.isEmpty()) {
            return true;
        }
        return rolls.size() == 1 && !isStrike();
    }

    private boolean isStrike() {
        return rolls.get(0).getPins() == 10;
    }

    private boolean isSpare() {
        return rolls.get(0).getPins() + rolls.get(1).getPins() == 10;
    }
}
