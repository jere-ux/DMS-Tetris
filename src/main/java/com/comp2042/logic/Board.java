package com.comp2042.logic;

import com.comp2042.logic.events.ClearRow;
import com.comp2042.view.ViewData;

/**
 * Defines the contract for a Tetris game board.
 * This interface outlines the core functionalities required for managing the game's state,
 * including brick movements, board updates, and game progression.
 */
public interface Board {
    /**
     * Moves the current brick down by one position.
     * @return true if the move was successful, false otherwise.
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick left by one position.
     * @return true if the move was successful, false otherwise.
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick right by one position.
     * @return true if the move was successful, false otherwise.
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick to the left.
     * @return true if the rotation was successful, false otherwise.
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick on the board.
     * @return true if the brick was created successfully, false if the game is over.
     */
    boolean createNewBrick();

    /**
     * Gets the current state of the game board as a matrix.
     * @return A 2D array representing the board.
     */
    int[][] getBoardMatrix();

    /**
     * Gets the data required for the view to render the current game state.
     * @return A {@link ViewData} object.
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the game board's background.
     */
    void mergeBrickToBackground();

    /**
     * Clears any completed rows from the board.
     * @return A {@link ClearRow} object containing information about the cleared rows.
     */
    ClearRow clearRows();

    /**
     * Gets the current score.
     * @return The {@link Score} object.
     */
    Score getScore();

    /**
     * Starts a new game.
     */
    void newGame();

    /**
     * Gets the y-coordinate of the ghost piece.
     * @return The y-position of the ghost piece.
     */
    int getGhostYPosition();

    /**
     * Holds the current brick, allowing the player to use it later.
     */
    void holdBrick();

    /**
     * Creates a pyramid-shaped obstacle on the board.
     */
    void createPyramidObstacle();

    /**
     * Handles the gravity effect, pulling bricks down.
     */
    void handleGravity();
}