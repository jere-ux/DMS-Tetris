package com.comp2042.logic.bricks;

import java.util.List;

public interface Brick {

    List<int[][]> getShapeMatrix();

    default boolean isBomb() {
        return false;
    }
}
