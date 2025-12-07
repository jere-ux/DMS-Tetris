package com.comp2042.logic;

import com.comp2042.controller.MenuController;
import com.comp2042.model.GameLevel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the player's score and high score.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final HighScoreManager highScoreManager;
    private final GameLevel.LevelType levelType;

    /**
     * Constructs a new Score object.
     * Initializes the high score from the high score manager.
     */
    public Score() {
        this.highScoreManager = new HighScoreManager();
        this.levelType = MenuController.getSelectedLevelType();
        highScore.setValue(highScoreManager.loadHighScore(levelType));
    }

    /**
     * Gets the score property.
     * @return The score property.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Gets the high score property.
     * @return The high score property.
     */
    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    /**
     * Adds a value to the current score.
     * @param value The value to add.
     */
    public void add(int value) {
        score.setValue(score.getValue() + value);
    }

    /**
     * Updates the high score if the current score is higher.
     */
    public void updateHighScore() {
        if (score.getValue() > highScore.getValue()) {
            highScore.setValue(score.getValue());
            highScoreManager.saveHighScore(levelType, highScore.getValue());
        }
    }

    /**
     * Resets the current score to zero.
     */
    public void reset() {
        score.setValue(0);
    }
}
