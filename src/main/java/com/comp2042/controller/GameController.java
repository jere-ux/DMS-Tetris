package com.comp2042.controller;

import com.comp2042.EventSource;
import com.comp2042.InputEventListener;
import com.comp2042.MoveEvent;
import com.comp2042.logic.Board;
import com.comp2042.logic.SimpleBoard;
import com.comp2042.logic.events.ClearRow;
import com.comp2042.view.DownData;
import com.comp2042.view.GuiController;
import com.comp2042.view.ViewData;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(10, 25);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        // Connect score to UI - score label will update automatically
        viewGuiController.bindScore(board.getScore().scoreProperty());
        // Connect high score to UI - high score label will update automatically
        viewGuiController.bindHighScore(board.getScore().highScoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            // Brick landed - merge it to the board
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            // Award bonus points for clearing lines
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                //  Save high score when game ends
                board.getScore().updateHighScore();
                viewGuiController.gameOver();

            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        } else {
            // Player manually moved piece down - give 1 point
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
        // Move brick down as far as possible
        while (board.moveBrickDown()) {
            distance++;
        }
        board.getScore().add(distance * 2);
        // Brick has landed - merge it to the board
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        // Award bonus points for clearing lines
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
        }
        if (board.createNewBrick()) {
            board.getScore().updateHighScore();
            viewGuiController.gameOver();
        }



        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        // Refresh brick view with new brick after game reset
        viewGuiController.refreshBrick(board.getViewData());
    }
}
