package com.comp2042.logic.bricks;

import com.comp2042.logic.SimpleBoard;

import java.util.Collections;
import java.util.List;

/**
 * Represents a bomb brick in the Tetris game.
 * When this brick is placed, it clears a 3x3 area around it.
 */
public class Bomb implements Brick {

    private final List<int[][]> shape;

    /**
     * Constructs a new Bomb brick.
     */
    public Bomb() {
        this.shape = Collections.singletonList(new int[][]{{SimpleBoard.BOMB_ID}});
    }

    /**
     * Gets the shape matrix of the bomb.
     * @return A list containing the bomb's shape matrix.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return shape;
    }

    /**
     * Checks if the brick is a bomb.
     * @return true, as this is a bomb brick.
     */
    @Override
    public boolean isBomb() {
        return true;
    }
}
