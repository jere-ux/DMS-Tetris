package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class RandomBrickGenerator implements BrickGenerator {

    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private final List<Brick> currentBag = new ArrayList<>();

    public RandomBrickGenerator() {
        fillBag();
        ensureQueueHasEnoughBricks(2);
    }
    // 7-bag system: creates a bag with all 7 pieces, shuffles them
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

    private void ensureQueueHasEnoughBricks(int minCount) {
        while (nextBricks.size() < minCount) {
            if (currentBag.isEmpty()) {
                fillBag();
            }
            nextBricks.add(currentBag.remove(0));
        }
    }

    @Override
    public Brick getBrick() {
        ensureQueueHasEnoughBricks(1);
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        ensureQueueHasEnoughBricks(1);
        return nextBricks.peek();
    }

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
}
