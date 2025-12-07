package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Represents a Tetris brick.
 * This interface defines the basic properties of a brick, such as its shape.
 */
public interface Brick {

    /**
     * Gets the shape matrix of the brick.
     * The list contains all possible rotations of the brick.
     * @return A list of 2D arrays representing the brick's shapes.
     */
    List<int[][]> getShapeMatrix();

    /**
     * Checks if the brick is a bomb.
     * @return true if the brick is a bomb, false otherwise.
     */
    default boolean isBomb() {
        return false;
    }
}
