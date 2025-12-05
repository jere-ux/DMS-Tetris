package com.comp2042.logic;

import com.comp2042.logic.events.ClearRow;
import com.comp2042.view.ViewData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    int getGhostYPosition();

    void holdBrick();
}
