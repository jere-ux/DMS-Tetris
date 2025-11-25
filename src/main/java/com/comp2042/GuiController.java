package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane nextBrickPanel1;

    @FXML
    private GridPane nextBrickPanel2;

    @FXML
    private GridPane nextBrickPanel3;

    @FXML
    private GameOverPanel gameOverPanel;

    // Label that displays the score on screen
    @FXML
    private Label scoreLabel;

    // Label that displays the high score on screen
    @FXML
    private Label highScoreLabel;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private int[][] storedNextBrickData1;
    private int[][] storedNextBrickData2;
    private int[][] storedNextBrickData3;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getCode() == KeyCode.P && isGameOver.getValue() == Boolean.FALSE) {
                    togglePause();
                    keyEvent.consume();
                }

                // Movement controls - only work when game is not paused or game is not over
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                }
                // New game - press N to start a new game (works even when game over)
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });
        gameOverPanel.setVisible(false);

        // Wire up New Game button to newGame() method
        gameOverPanel.setOnNewGameButtonClick(e -> newGame(e));

        // Start score at 0
        if (scoreLabel != null) {
            scoreLabel.setText("0");
        }

        // Start high score at 0
        if (highScoreLabel != null) {
            highScoreLabel.setText("0");
        }

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    // Initializes the game board display and starts the automatic falling animation
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        Point2D offset = getGamePanelOffset();
        brickPanel.setTranslateX(offset.getX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setTranslateY(offset.getY() - 42 + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        refreshNextBricks(brick.getNextThreeBricks());

        // Game loop - automatically moves bricks down every 400ms
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    private void refreshNextBrickPanel(GridPane panel, int[][] nextBrickData, int[][] storedData) {
        if (panel == null || nextBrickData == null) return;

        // Simple check if data changed
        boolean changed = false;
        if (storedData == null || nextBrickData.length != storedData.length || nextBrickData[0].length != storedData[0].length) {
            changed = true;
        } else {
            for (int i = 0; i < nextBrickData.length; i++) {
                for (int j = 0; j < nextBrickData[i].length; j++) {
                    if (nextBrickData[i][j] != storedData[i][j]) {
                        changed = true;
                        break;
                    }
                }
                if (changed) break;
            }
        }

        if (!changed) return;

        panel.getChildren().clear();

        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(nextBrickData[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                panel.add(rectangle, j, i);
            }
        }
    }

    private void refreshNextBricks(java.util.List<int[][]> nextThreeBricks) {
        if (nextThreeBricks == null || nextThreeBricks.size() < 3) return;

        refreshNextBrickPanel(nextBrickPanel1, nextThreeBricks.get(0), storedNextBrickData1);
        storedNextBrickData1 = nextThreeBricks.get(0);

        refreshNextBrickPanel(nextBrickPanel2, nextThreeBricks.get(1), storedNextBrickData2);
        storedNextBrickData2 = nextThreeBricks.get(1);

        refreshNextBrickPanel(nextBrickPanel3, nextThreeBricks.get(2), storedNextBrickData3);
        storedNextBrickData3 = nextThreeBricks.get(2);
    }

    // Updates the falling brick display on screen
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            refreshNextBricks(brick.getNextThreeBricks());
            Point2D offset = getGamePanelOffset();
            brickPanel.setTranslateX(offset.getX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setTranslateY(offset.getY() - 42 + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    // Moves the current brick down one row (called by timer or player input)
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                // Show bonus points notification when lines are cleared
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    // Connects the score value to the label - label update
    public void bindScore(IntegerProperty scoreProperty) {
        if (scoreLabel != null && scoreProperty != null) {
            scoreLabel.textProperty().bind(scoreProperty.asString("%d"));
        }
    }

    // Connects the high score value to the label - label update
    public void bindHighScore(IntegerProperty highScoreProperty) {
        if (highScoreLabel != null && highScoreProperty != null) {
            highScoreLabel.textProperty().bind(highScoreProperty.asString("%d"));
        }
    }

    // Called when game ends - stops animation and shows game over panel
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    // Starts a new game - resets board, score, and restarts animation
    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    // Toggles pause state and stops/resumes game timeline
    public void togglePause() {
        if (timeLine != null && !isGameOver.getValue()) {
            if (isPause.getValue()) {
                // Resume game
                isPause.setValue(Boolean.FALSE);
                timeLine.play();
            } else {
                // Pause game
                isPause.setValue(Boolean.TRUE);
                timeLine.pause();
            }
        }
        gamePanel.requestFocus();
    }

    public void pauseGame(ActionEvent actionEvent) {
        togglePause();
    }

    private Point2D getGamePanelOffset() {
        if (gamePanel == null || brickPanel == null || gamePanel.getScene() == null) {
            return Point2D.ZERO;
        }
        Point2D scenePoint = gamePanel.localToScene(0, 0);
        Node parent = brickPanel.getParent();
        if (parent == null) {
            return scenePoint;
        }
        return parent.sceneToLocal(scenePoint);
    }
}
