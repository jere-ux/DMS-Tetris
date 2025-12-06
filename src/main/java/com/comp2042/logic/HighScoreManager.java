package com.comp2042.logic;

import com.comp2042.model.GameLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class HighScoreManager {

    private static final String HIGH_SCORE_FILE = "highscores.properties";
    private final Properties highScores = new Properties();

    public HighScoreManager() {
        loadHighScores();
    }

    private void loadHighScores() {
        try (FileInputStream in = new FileInputStream(HIGH_SCORE_FILE)) {
            highScores.load(in);
        } catch (IOException e) {
            // It's okay if the file doesn't exist yet
        }
    }

    public int loadHighScore(GameLevel.LevelType levelType) {
        String score = highScores.getProperty(levelType.name(), "0");
        return Integer.parseInt(score);
    }

    public void saveHighScore(GameLevel.LevelType levelType, int score) {
        highScores.setProperty(levelType.name(), String.valueOf(score));
        try (FileOutputStream out = new FileOutputStream(HIGH_SCORE_FILE)) {
            highScores.store(out, "Tetris High Scores");
        } catch (IOException e) {
            System.err.println("Could not save high scores to file: " + e.getMessage());
        }
    }
}