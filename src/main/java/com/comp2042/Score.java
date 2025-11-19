package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

// Score class - Stores and manages the player's score
public final class Score {

    // JavaFX property that automatically updates the UI when score changes
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    // Returns the score property so UI can connect to it
    public IntegerProperty scoreProperty() {
        return score;
    }

    // Adds points to the current score
    public void add(int value) {
        score.setValue(score.getValue() + value);
    }

    // Resets score to 0 for a new game
    public void reset() {
        score.setValue(0);
    }
}
