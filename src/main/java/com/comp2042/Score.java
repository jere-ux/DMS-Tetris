package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Score class - Stores and manages the player's score
public final class Score {

    //  property that automatically updates the UI when score changes
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    //  property for high score that persists across games
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);

    private static final String HIGH_SCORE_FILE = "highscore.txt";


    public Score() {
        loadHighScore();
    }

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
            saveHighScore();
        }
    }


    // Resets score to 0 for a new game
    public void reset() {
        score.setValue(0);
    }

    private void loadHighScore() {
        try {
            File file = new File(HIGH_SCORE_FILE);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextInt()) {
                    highScore.setValue(scanner.nextInt());
                }
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            // File not found, high score will be 0
        }
    }

    private void saveHighScore() {
        try {
            FileWriter writer = new FileWriter(HIGH_SCORE_FILE);
            writer.write(String.valueOf(highScore.getValue()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
