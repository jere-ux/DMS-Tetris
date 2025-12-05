package com.comp2042.logic;

import com.comp2042.controller.MenuController;
import com.comp2042.model.GameLevel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final HighScoreManager highScoreManager;
    private final GameLevel.LevelType levelType;

    public Score() {
        this.highScoreManager = new HighScoreManager();
        this.levelType = MenuController.getSelectedLevelType();
        highScore.setValue(highScoreManager.loadHighScore(levelType));
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
            highScoreManager.saveHighScore(levelType, highScore.getValue());
        }
    }

    public void reset() {
        score.setValue(0);
    }
}
