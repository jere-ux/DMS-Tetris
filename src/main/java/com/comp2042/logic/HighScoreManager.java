package com.comp2042.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HighScoreManager {

    private static final String HIGH_SCORE_FILE = "highscore.txt";

    public int loadHighScore() {
        try {
            File file = new File(HIGH_SCORE_FILE);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextInt()) {
                    return scanner.nextInt();
                }
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("High score file not found. A new one will be created.");
        }
        return 0;
    }

    public void saveHighScore(int highScore) {
        try {
            FileWriter writer = new FileWriter(HIGH_SCORE_FILE);
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (IOException e) {
            System.err.println("Could not save high score to file: " + e.getMessage());
        }
    }
}
