package com.comp2042.logic.events;

import com.comp2042.logic.helper.MatrixOperations;

/**
 * Contains the result of a row clearing event.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Creates a new ClearRow event.
     * @param linesRemoved The number of lines cleared.
     * @param newMatrix The new game board state.
     * @param scoreBonus The score bonus awarded.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Returns the number of lines cleared.
     * @return The number of lines cleared.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns the new game board after clearing rows.
     * @return A copy of the new matrix.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the score bonus for this event.
     * @return The score bonus.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
