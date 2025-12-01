package com.comp2042;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void add() {
        Score score = new Score();
        score.add(100);
        assertEquals(100, score.scoreProperty().get(), "Score should update to 100");
    }

    @Test
    void updateHighScore() {
        Score score = new Score();
        score.add(1000);
        score.updateHighScore();
        assertEquals(1000, score.highScoreProperty().get(), "High score should be saved");
    }

    @Test
    void reset() {
        Score score = new Score();
        score.add(50);
        score.reset();
        assertEquals(0, score.scoreProperty().get(), "Score should reset to 0");
    }
}