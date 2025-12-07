package com.comp2042.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Manages leaderboards for different game levels.
 * This class handles loading, saving, and updating leaderboard scores.
 */
public class LeaderboardManager {

    private static final String LEADERBOARD_FILE = "leaderboard.dat";
    private Map<GameLevel.LevelType, List<ScoreEntry>> leaderboards;

    /**
     * Constructs a new LeaderboardManager and loads existing scores.
     */
    public LeaderboardManager() {
        this.leaderboards = new EnumMap<>(GameLevel.LevelType.class);
        for (GameLevel.LevelType type : GameLevel.LevelType.values()) {
            leaderboards.put(type, new ArrayList<>());
        }
        loadScores();
    }

    /**
     * Adds a new score to the leaderboard for a specific game level.
     * @param levelType The game level.
     * @param name The player's name.
     * @param score The player's score.
     */
    public void addScore(GameLevel.LevelType levelType, String name, int score) {
        List<ScoreEntry> leaderboard = leaderboards.get(levelType);
        leaderboard.add(new ScoreEntry(name, score));
        Collections.sort(leaderboard);
        saveScores();
    }

    /**
     * Gets the list of scores for a specific game level.
     * @param levelType The game level.
     * @return A list of ScoreEntry objects.
     */
    public List<ScoreEntry> getScores(GameLevel.LevelType levelType) {
        return leaderboards.get(levelType);
    }

    /**
     * Loads the leaderboards from a file.
     */
    @SuppressWarnings("unchecked")
    public void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LEADERBOARD_FILE))) {
            leaderboards = (Map<GameLevel.LevelType, List<ScoreEntry>>) ois.readObject();
        } catch (FileNotFoundException e) {
            // It's okay if the file doesn't exist yet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the leaderboards to a file.
     */
    public void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LEADERBOARD_FILE))) {
            oos.writeObject(leaderboards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
