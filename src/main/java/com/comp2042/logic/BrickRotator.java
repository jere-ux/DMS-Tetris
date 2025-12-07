package com.comp2042.logic;

import com.comp2042.model.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;

/**
 * Handles the rotation of a Tetris brick.
 * This class keeps track of the current shape of a brick and provides methods to get the next rotated shape.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Gets the next shape of the brick after a rotation.
     * @return A {@link NextShapeInfo} object containing the next shape and its position.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the current shape of the brick.
     * @return A 2D array representing the current shape.
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current shape of the brick.
     * @param currentShape The index of the current shape.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick to be rotated.
     * @param brick The brick to be rotated.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


    /**
     * Gets the current brick.
     * @return The current brick.
     */
    public Brick getBrick() {
        return brick;
    }
}
