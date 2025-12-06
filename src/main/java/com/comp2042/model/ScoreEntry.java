package com.comp2042.model;

import java.io.Serializable;

public class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final int score;

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(other.score, this.score); // Sort descending
    }

    @Override
    public String toString() {
        return name + ": " + score;
    }
}
