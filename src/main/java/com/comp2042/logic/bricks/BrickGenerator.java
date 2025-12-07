package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Defines the contract for a brick generator.
 * A brick generator is responsible for creating and providing new bricks for the game.
 */
public interface BrickGenerator {

    /**
     * Gets the current brick and generates a new next brick.
     * @return The current brick.
     */
    Brick getBrick();

    /**
     * Gets the next brick in the sequence without advancing the generator.
     * @return The next brick.
     */
    Brick getNextBrick();

    /**
     * Gets a list of the next bricks in the sequence.
     * @param count The number of next bricks to retrieve.
     * @return A list of the next bricks.
     */
    List<Brick> getNextBricks(int count);
}
