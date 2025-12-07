package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.logic.events.ClearRow;
import com.comp2042.logic.helper.BrickRotator;
import com.comp2042.model.NextShapeInfo;
import com.comp2042.view.ViewData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple implementation of the Tetris game board.
 * This class manages the game's state, including the position of the current brick,
 * the game matrix, and the player's score.
 */
public class SimpleBoard implements Board {

    public static final int BOMB_ID = 8;
    private final int width;
    private final int height;
    private final RandomBrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick;
    private boolean canHold = true;

    /**
     * Constructs a new SimpleBoard.
     * @param width The width of the board.
     * @param height The height of the board.
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[height][width];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Attempts to move the current brick down.
     * @return true if successful, false if there is a collision.
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            canHold = true;
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current brick left.
     * @return true if successful, false if there is a collision.
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current brick right.
     * @return true if successful, false if there is a collision.
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to rotate the current brick.
     * @return true if successful, false if there is a collision.
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /**
     * Creates a new brick at the top of the board.
     * @return true if the new brick overlaps with existing blocks (game over), false otherwise.
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point((width - currentBrick.getShapeMatrix().get(0)[0].length) / 2, 0);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        List<int[][]> nextThreeBricks = new ArrayList<>();
        for (Brick brick : brickGenerator.getNextBricks(3)) {
            nextThreeBricks.add(brick.getShapeMatrix().get(0));
        }
        int[][] holdBrickData = heldBrick != null ? heldBrick.getShapeMatrix().get(0) : null;
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), nextThreeBricks, getGhostYPosition(), holdBrickData);
    }

    /**
     * Merges the current brick into the board. If the brick is a bomb, it detonates.
     */
    @Override
    public void mergeBrickToBackground() {
        if (isCurrentBrickBomb()) {
            Point bombCenter = getBombCenter();
            detonateBomb((int) bombCenter.getX(), (int) bombCenter.getY());
        } else {
            currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        }
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Resets the board for a new game.
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();
        heldBrick = null;
        canHold = true;
        brickGenerator.resetPowerUpProgress();
        createNewBrick();
    }

    @Override
    public int getGhostYPosition() {
        int ghostY = (int) currentOffset.getY();
        while (!MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), ghostY + 1)) {
            ghostY++;
        }
        return ghostY;
    }

    /**
     * Holds the current brick or swaps with the held brick.
     */
    @Override
    public void holdBrick() {
        if (!canHold) {
            return;
        }
        if (heldBrick == null) {
            heldBrick = brickRotator.getBrick();
            createNewBrick();
        } else {
            Brick tmp = brickRotator.getBrick();
            brickRotator.setBrick(heldBrick);
            heldBrick = tmp;
            currentOffset = new Point(4, 0);
        }
        canHold = false;
    }

    /**
     * Creates a pyramid-shaped obstacle at the bottom of the board.
     */
    @Override
    public void createPyramidObstacle() {
        int pyramidHeight = 4;
        Random random = new Random();
        for (int i = 0; i < pyramidHeight; i++) {
            for (int j = 0; j < i * 2 + 1; j++) {
                int x = (width / 2) - i + j - 1;
                int y = height - pyramidHeight + i;
                if (x >= 0 && x < width) {
                    // Assign a random color (1-7) to each block
                    currentGameMatrix[y][x] = random.nextInt(7) + 1;
                }
            }
        }
    }

    /**
     * Checks if the current brick is a bomb.
     * @return true if the current brick is a bomb, false otherwise.
     */
    public boolean isCurrentBrickBomb() {
        return brickRotator.getBrick().isBomb();
    }

    /**
     * Gets the position of the current brick.
     * @return The current brick's position.
     */
    public Point getBrickPosition() {
        return currentOffset;
    }

    /**
     * Detonates a bomb at the specified center coordinates, clearing a 3x3 area.
     * @param centerX The x-coordinate of the bomb's center.
     * @param centerY The y-coordinate of the bomb's center.
     * @return A list of points representing the cleared blocks.
     */
    public List<Point> detonateBomb(int centerX, int centerY) {
        List<Point> clearedBlocks = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = centerX + j;
                int y = centerY + i;

                if (y >= 0 && y < height && x >= 0 && x < width) {
                    if (currentGameMatrix[y][x] != 0) {
                        clearedBlocks.add(new Point(x, y));
                    }
                    currentGameMatrix[y][x] = 0;
                }
            }
        }
        return clearedBlocks;
    }

    /**
     * Finds the center of the bomb brick.
     * @return The center point of the bomb.
     */
    private Point getBombCenter() {
        int[][] shape = brickRotator.getCurrentShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == BOMB_ID) {
                    return new Point(currentOffset.x + j, currentOffset.y + i);
                }
            }
        }
        return currentOffset; // Fallback
    }

    /**
     * Applies gravity to the board, causing unsupported blocks to fall.
     */
    @Override
    public void handleGravity() {
        for (int j = 0; j < width; j++) {
            int emptyRow = -1;
            for (int i = height - 1; i >= 0; i--) {
                if (currentGameMatrix[i][j] == 0 && emptyRow == -1) {
                    emptyRow = i;
                } else if (currentGameMatrix[i][j] != 0 && emptyRow != -1) {
                    currentGameMatrix[emptyRow][j] = currentGameMatrix[i][j];
                    currentGameMatrix[i][j] = 0;
                    emptyRow--;
                }
            }
        }
    }

    /**
     * Gets the brick generator.
     * @return The brick generator.
     */
    public RandomBrickGenerator getBrickGenerator() {
        return brickGenerator;
    }
}
