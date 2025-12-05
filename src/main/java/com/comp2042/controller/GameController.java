package com.comp2042.controller;

import com.comp2042.logic.SimpleBoard;
import com.comp2042.logic.events.EventSource;
import com.comp2042.logic.Board;
import com.comp2042.logic.events.EventType;
import com.comp2042.logic.events.MoveEvent;
import com.comp2042.model.GameLevel;
import com.comp2042.view.DownData;
import com.comp2042.view.GuiController;
import com.comp2042.view.ViewData;
import com.comp2042.logic.events.ClearRow;
import javafx.animation.AnimationTimer;

import java.awt.*;
import java.util.List;

public class GameController implements InputEventListener {

    private final SimpleBoard board;
    private final GuiController viewGuiController;
    private AnimationTimer gameTimer;
    private long lastUpdate = 0;
    private double dropInterval = 1_000_000_000; // 1 second in nanoseconds
    private int linesClearedSinceSpeedUp = 0;

    public GameController(GuiController c) {
        viewGuiController = c;
        board = new SimpleBoard(10, 25);
        // Obstacle Mode: Create a pyramid at the start
        if (MenuController.getSelectedLevelType() == GameLevel.LevelType.TYPE_C_OBSTACLES) {
            board.createPyramidObstacle();
            viewGuiController.setPowerUpContainerVisibility(true);
        }
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        // Refresh the view to show the pyramid immediately
        if (MenuController.getSelectedLevelType() == GameLevel.LevelType.TYPE_C_OBSTACLES) {
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindHighScore(board.getScore().highScoreProperty());
        lastUpdate = System.nanoTime(); // Initialize timer
        startGameLoop();
    }

    private void startGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= dropInterval) {
                    onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                    lastUpdate += dropInterval;

                    // To prevent a "death spiral" on lag, reset the timer if it falls too far behind.
                    // This prioritizes responsiveness over catching up on missed drops.
                    if (now - lastUpdate >= dropInterval) {
                        lastUpdate = now;
                    }
                }
            }
        };
        gameTimer.start();
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            if (board.isCurrentBrickBomb()) {
                Point bombCenter = board.getBrickPosition();
                List<Point> clearedBlocks = board.detonateBomb(bombCenter.x, bombCenter.y);
                viewGuiController.spawnFireEffect(bombCenter.x, bombCenter.y);
                viewGuiController.animateBlockRemoval(clearedBlocks, () -> handleBombAftermath(clearedBlocks.size()));
            } else {
                board.mergeBrickToBackground();
                clearRow = board.clearRows();
                if (clearRow.getLinesRemoved() > 0) {
                    board.getScore().add(clearRow.getScoreBonus());
                    linesClearedSinceSpeedUp += clearRow.getLinesRemoved();
                    if (MenuController.getSelectedLevelType() == GameLevel.LevelType.TYPE_C_OBSTACLES) {
                        board.getBrickGenerator().incrementPowerUpProgress(clearRow.getLinesRemoved());
                        viewGuiController.updatePowerUpProgressBar(board.getBrickGenerator().getPowerUpProgress() / 5.0);
                    }
                    // Speed Curve Mode: Increase speed every 2 lines
                    if (MenuController.getSelectedLevelType() == GameLevel.LevelType.TYPE_A_SPEED_CURVE && linesClearedSinceSpeedUp >= 2) {
                        dropInterval *= 0.85; // 15% faster
                        linesClearedSinceSpeedUp = 0;
                        viewGuiController.showSpeedNotification();
                    }
                }
                if (board.createNewBrick()) {
                    board.getScore().updateHighScore();
                    viewGuiController.gameOver();
                    gameTimer.stop();
                }
                viewGuiController.refreshGameBackground(board.getBoardMatrix());
            }
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        board.holdBrick();
        return board.getViewData();
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        int distance = 0;
        while (board.moveBrickDown()) {
            distance++;
        }
        board.getScore().add(distance * 2);

        if (board.isCurrentBrickBomb()) {
            Point bombCenter = board.getBrickPosition();
            List<Point> clearedBlocks = board.detonateBomb(bombCenter.x, bombCenter.y);
            viewGuiController.spawnFireEffect(bombCenter.x, bombCenter.y);
            viewGuiController.animateBlockRemoval(clearedBlocks, () -> handleBombAftermath(clearedBlocks.size()));
        } else {
            board.mergeBrickToBackground();
            ClearRow clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
                linesClearedSinceSpeedUp += clearRow.getLinesRemoved();
                if (MenuController.getSelectedLevelType() == GameLevel.LevelType.TYPE_C_OBSTACLES) {
                    board.getBrickGenerator().incrementPowerUpProgress(clearRow.getLinesRemoved());
                    viewGuiController.updatePowerUpProgressBar(board.getBrickGenerator().getPowerUpProgress() / 5.0);
                }
                // Speed Curve Mode: Increase speed every 2 lines
                if (MenuController.getSelectedLevelType() == GameLevel.LevelType.TYPE_A_SPEED_CURVE && linesClearedSinceSpeedUp >= 2) {
                    dropInterval *= 0.85;
                    linesClearedSinceSpeedUp = 0;
                    viewGuiController.showSpeedNotification();
                }
            }
            if (board.createNewBrick()) {
                board.getScore().updateHighScore();
                viewGuiController.gameOver();
                gameTimer.stop();
            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
        return new DownData(null, board.getViewData());
    }

    @Override
    public void createNewGame() {
        board.newGame();
        // Obstacle Mode: Create a pyramid at the start of a new game
        if (MenuController.getSelectedLevelType() == GameLevel.LevelType.TYPE_C_OBSTACLES) {
            board.createPyramidObstacle();
            viewGuiController.setPowerUpContainerVisibility(true);
        } else {
            viewGuiController.setPowerUpContainerVisibility(false);
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshBrick(board.getViewData());
        dropInterval = 1_000_000_000;
        linesClearedSinceSpeedUp = 0;
        viewGuiController.updatePowerUpProgressBar(0);
        lastUpdate = System.nanoTime(); // Reset timer for new game
        gameTimer.start();
    }

    private void handleBombAftermath(int destroyedBlocks) {
        board.getScore().add(destroyedBlocks * 50);
        board.handleGravity();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
        if (board.createNewBrick()) {
            board.getScore().updateHighScore();
            viewGuiController.gameOver();
            gameTimer.stop();
        }
    }
}