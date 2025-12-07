package com.comp2042.view;

import com.comp2042.logic.helper.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents data for rendering the game state.
 */
public final class ViewData {

    /** The current brick's shape and color matrix. */
    private final int[][] brickData;
    /** The current brick's x-coordinate. */
    private final int xPosition;
    /** The current brick's y-coordinate. */
    private final int yPosition;
    /** The matrix for the next brick. */
    private final int[][] nextBrickData;
    /** A list of matrices for the next three bricks. */
    private final List<int[][]> nextThreeBricks;
    /** The y-coordinate for the ghost piece. */
    private final int ghostYPosition;
    /** The matrix for the held brick. */
    private final int[][] holdBrickData;

    /**
     * Constructs a ViewData object.
     *
     * @param brickData     The current brick's matrix.
     * @param xPosition     The current brick's x-coordinate.
     * @param yPosition     The current brick's y-coordinate.
     * @param nextBrickData The next brick's matrix.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextThreeBricks = new ArrayList<>();
        this.ghostYPosition = yPosition; // Default to same position
        this.holdBrickData = null;
    }

    /**
     * Constructs a ViewData object.
     *
     * @param brickData       The current brick's matrix.
     * @param xPosition       The current brick's x-coordinate.
     * @param yPosition       The current brick's y-coordinate.
     * @param nextBrickData   The next brick's matrix.
     * @param nextThreeBricks A list of the next three bricks.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, List<int[][]> nextThreeBricks) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextThreeBricks = nextThreeBricks != null ? new ArrayList<>(nextThreeBricks) : new ArrayList<>();
        this.ghostYPosition = yPosition; // Default to same position
        this.holdBrickData = null;
    }

    /**
     * Constructs a ViewData object.
     *
     * @param brickData       The current brick's matrix.
     * @param xPosition       The current brick's x-coordinate.
     * @param yPosition       The current brick's y-coordinate.
     * @param nextBrickData   The next brick's matrix.
     * @param nextThreeBricks A list of the next three bricks.
     * @param ghostYPosition  The ghost piece's y-coordinate.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, List<int[][]> nextThreeBricks, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextThreeBricks = nextThreeBricks != null ? new ArrayList<>(nextThreeBricks) : new ArrayList<>();
        this.ghostYPosition = ghostYPosition;
        this.holdBrickData = null;
    }

    /**
     * Constructs a ViewData object.
     *
     * @param brickData       The current brick's matrix.
     * @param xPosition       The current brick's x-coordinate.
     * @param yPosition       The current brick's y-coordinate.
     * @param nextThreeBricks A list of the next three bricks.
     * @param ghostYPosition  The ghost piece's y-coordinate.
     * @param holdBrickData   The held brick's matrix.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextThreeBricks, int ghostYPosition, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = null; // Not used anymore
        this.nextThreeBricks = nextThreeBricks != null ? new ArrayList<>(nextThreeBricks) : new ArrayList<>();
        this.ghostYPosition = ghostYPosition;
        this.holdBrickData = holdBrickData;
    }

    /**
     * Gets a copy of the current brick's data.
     *
     * @return A copy of the brick's matrix.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the x-coordinate of the current brick.
     *
     * @return The x-position.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y-coordinate of the current brick.
     *
     * @return The y-position.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets a copy of the next brick's data.
     *
     * @return A copy of the next brick's matrix.
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets copies of the next three bricks' data.
     *
     * @return A list of copies for the next three bricks.
     */
    public List<int[][]> getNextThreeBricks() {
        List<int[][]> result = new ArrayList<>();
        for (int[][] brick : nextThreeBricks) {
            result.add(MatrixOperations.copy(brick));
        }
        return result;
    }

    /**
     * Gets the y-coordinate of the ghost piece.
     *
     * @return The ghost piece's y-position.
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }

    /**
     * Gets a copy of the held brick's data.
     *
     * @return A copy of the held brick's matrix, or null if no brick is held.
     */
    public int[][] getHoldBrickData() {
        return holdBrickData != null ? MatrixOperations.copy(holdBrickData) : null;
    }
}
