package com.comp2042.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final HighScoreManager highScoreManager;

    public Score() {
        this.highScoreManager = new HighScoreManager();
        highScore.setValue(highScoreManager.loadHighScore());
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    public void add(int value) {
        score.setValue(score.getValue() + value);
    }

    public void updateHighScore() {
        if (score.getValue() > highScore.getValue()) {
            highScore.setValue(score.getValue());
            highScoreManager.saveHighScore(highScore.getValue());
        }
    }

    public void reset() {
        score.setValue(0);
    }
}
