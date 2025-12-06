package com.comp2042.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class LeaderboardManager {

    private static final String LEADERBOARD_FILE = "leaderboard.dat";
    private Map<GameLevel.LevelType, List<ScoreEntry>> leaderboards;

    public LeaderboardManager() {
        this.leaderboards = new EnumMap<>(GameLevel.LevelType.class);
        for (GameLevel.LevelType type : GameLevel.LevelType.values()) {
            leaderboards.put(type, new ArrayList<>());
        }
        loadScores();
    }

    public void addScore(GameLevel.LevelType levelType, String name, int score) {
        List<ScoreEntry> leaderboard = leaderboards.get(levelType);
        leaderboard.add(new ScoreEntry(name, score));
        Collections.sort(leaderboard);
        saveScores();
    }

    public List<ScoreEntry> getScores(GameLevel.LevelType levelType) {
        return leaderboards.get(levelType);
    }

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

    public void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LEADERBOARD_FILE))) {
            oos.writeObject(leaderboards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}