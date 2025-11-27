package com.comp2042;

import java.util.ArrayList;
import java.util.List;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final List<int[][]> nextThreeBricks;
    private final int ghostYPosition;
    private final int[][] holdBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextThreeBricks = new ArrayList<>();
        this.ghostYPosition = yPosition; // Default to same position
        this.holdBrickData = null;
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, List<int[][]> nextThreeBricks) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextThreeBricks = nextThreeBricks != null ? new ArrayList<>(nextThreeBricks) : new ArrayList<>();
        this.ghostYPosition = yPosition; // Default to same position
        this.holdBrickData = null;
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, List<int[][]> nextThreeBricks, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextThreeBricks = nextThreeBricks != null ? new ArrayList<>(nextThreeBricks) : new ArrayList<>();
        this.ghostYPosition = ghostYPosition;
        this.holdBrickData = null;
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextThreeBricks, int ghostYPosition, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = null; // Not used anymore
        this.nextThreeBricks = nextThreeBricks != null ? new ArrayList<>(nextThreeBricks) : new ArrayList<>();
        this.ghostYPosition = ghostYPosition;
        this.holdBrickData = holdBrickData;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    public List<int[][]> getNextThreeBricks() {
        List<int[][]> result = new ArrayList<>();
        for (int[][] brick : nextThreeBricks) {
            result.add(MatrixOperations.copy(brick));
        }
        return result;
    }

    public int getGhostYPosition() {
        return ghostYPosition;
    }

    public int[][] getHoldBrickData() {
        return holdBrickData != null ? MatrixOperations.copy(holdBrickData) : null;
    }
}
