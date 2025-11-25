package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

// Score class - Stores and manages the player's score
public final class Score {

    //  property that automatically updates the UI when score changes
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    //  property for high score that persists across games
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);

    // Returns the score property so UI can connect to it
    public IntegerProperty scoreProperty() {
        return score;
    }

    // Returns the high score property so UI can connect to it
    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    // Adds points to the current score
    public void add(int value) {
        int newScore = score.getValue() + value;
        score.setValue(newScore);
    }

    // Call this when game ends to update high score
    public void updateHighScore() {
        if (score.getValue() > highScore.getValue()) {
            highScore.setValue(score.getValue());
        }
    }


    // Resets score to 0 for a new game
    public void reset() {
        score.setValue(0);
    }
}
