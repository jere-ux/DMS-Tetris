package com.comp2042.logic.bricks;

import com.comp2042.controller.MenuController;
import com.comp2042.model.GameLevel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * A brick generator that creates a random sequence of bricks using a 7-bag system.
 * It also manages the logic for power-ups like the bomb.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private final List<Brick> currentBag = new ArrayList<>();
    private int powerUpProgress = 0;

    /**
     * Constructs a new RandomBrickGenerator.
     */
    public RandomBrickGenerator() {
        fillBag();
        ensureQueueHasEnoughBricks(2);
    }

    /**
     * Fills the bag with a new set of 7 shuffled bricks.
     */
    private void fillBag() {
        currentBag.clear();
        currentBag.add(new IBrick());
        currentBag.add(new JBrick());
        currentBag.add(new LBrick());
        currentBag.add(new OBrick());
        currentBag.add(new SBrick());
        currentBag.add(new TBrick());
        currentBag.add(new ZBrick());
        Collections.shuffle(currentBag);
    }

    /**
     * Ensures that the queue of next bricks has at least a minimum number of bricks.
     * @param minCount The minimum number of bricks required in the queue.
     */
    private void ensureQueueHasEnoughBricks(int minCount) {
        while (nextBricks.size() < minCount) {
            if (currentBag.isEmpty()) {
                fillBag();
            }
            nextBricks.add(currentBag.remove(0));
        }
    }

    /**
     * Gets the next brick from the generator. If a power-up is ready, it returns a bomb.
     * @return The next brick.
     */
    @Override
    public Brick getBrick() {
        if (MenuController.getSelectedLevelType() == GameLevel.LevelType.TYPE_C_OBSTACLES && powerUpProgress >= 5) {
            powerUpProgress = 0;
            return new Bomb();
        }
        ensureQueueHasEnoughBricks(1);
        return nextBricks.poll();
    }

    /**
     * Peeks at the next brick in the sequence without removing it.
     * @return The next brick.
     */
    @Override
    public Brick getNextBrick() {
        ensureQueueHasEnoughBricks(1);
        return nextBricks.peek();
    }

    /**
     * Gets a list of the next bricks in the sequence.
     * @param count The number of bricks to retrieve.
     * @return A list of the next bricks.
     */
    @Override
    public List<Brick> getNextBricks(int count) {
        ensureQueueHasEnoughBricks(count);
        List<Brick> result = new ArrayList<>();
        int i = 0;
        for (Brick brick : nextBricks) {
            if (i >= count) break;
            result.add(brick);
            i++;
        }
        return result;
    }

    /**
     * Increments the power-up progress.
     * @param linesCleared The number of lines cleared.
     */
    public void incrementPowerUpProgress(int linesCleared) {
        powerUpProgress += linesCleared;
    }

    /**
     * Gets the current power-up progress.
     * @return The power-up progress.
     */
    public int getPowerUpProgress() {
        return powerUpProgress;
    }

    /**
     * Resets the power-up progress to zero.
     */
    public void resetPowerUpProgress() {
        powerUpProgress = 0;
    }
}
