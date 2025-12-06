package com.comp2042.logic.bricks;

import com.comp2042.logic.SimpleBoard;

import java.util.Collections;
import java.util.List;

public class Bomb implements Brick {

    private final List<int[][]> shape;

    public Bomb() {
        this.shape = Collections.singletonList(new int[][]{{SimpleBoard.BOMB_ID}});
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return shape;
    }

    @Override
    public boolean isBomb() {
        return true;
    }
}
