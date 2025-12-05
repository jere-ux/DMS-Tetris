package com.comp2042.view;

import com.comp2042.controller.InputEventListener;
import com.comp2042.logic.events.ClearRow;
import com.comp2042.logic.events.EventSource;
import com.comp2042.logic.events.EventType;
import com.comp2042.logic.events.MoveEvent;
import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;
    @FXML
    private BorderPane gameBoard;

    @FXML
    private StackPane notificationPane;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane ghostBrickPanel;

    @FXML
    private GridPane nextBrickPanel1;

    @FXML
    private GridPane nextBrickPanel2;

    @FXML
    private GridPane nextBrickPanel3;

    @FXML
    private GridPane holdBrickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private PauseMenuPanel pauseMenuPanel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label highScoreLabel;

    @FXML
    private Label speedNotificationLabel;

    @FXML
    private ProgressBar powerUpProgressBar;

    @FXML
    private VBox powerUpContainer;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Rectangle[][] ghostRectangles;

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

                if (keyEvent.getCode() == KeyCode.P && !isGameOver.getValue()) {
                    togglePause();
                    keyEvent.consume();
                }

                if (!isPause.getValue() && !isGameOver.getValue()) {
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
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        DownData downData = eventListener.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                        ClearRow clearRow = downData.getClearRow();
                        if (clearRow != null) {
                            if (clearRow.getLinesRemoved() > 0) {
                                NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.getScoreBonus());
                                notificationPane.getChildren().add(notificationPanel);
                                notificationPanel.showScore(notificationPane.getChildren());
                            }
                            refreshGameBackground(clearRow.getNewMatrix());
                        }
                        refreshBrick(downData.getViewData());
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.C) {
                        refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });
        gameOverPanel.setVisible(false);

        gameOverPanel.setOnNewGameButtonClick(e -> newGame(e));
        gameOverPanel.setOnMainMenuButtonClick(this::returnToMainMenu);


        // Initialize pause menu panel
        if (pauseMenuPanel != null) {
            pauseMenuPanel.setVisible(false);
            pauseMenuPanel.setOnResumeButtonClick(e -> togglePause());
            pauseMenuPanel.setOnNewGameButtonClick(e -> newGame(e));
        }

        if (scoreLabel != null) {
            scoreLabel.setText("0");
        }

        if (highScoreLabel != null) {
            highScoreLabel.setText("0");
        }
        // Add reflection effect to the game board
        if (gameBoard != null) {
            Reflection reflection = new Reflection();
            reflection.setFraction(0.4);
            reflection.setTopOffset(2.0);
            gameBoard.setEffect(reflection);
        }


        gameBoard.setStyle("-fx-border-color: linear-gradient(#2A5058, #61a2b1); -fx-border-width: 12px; -fx-border-radius: 12px;");
        gamePanel.setGridLinesVisible(true);
    }

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
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);

                Rectangle ghostRectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghostRectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                ghostRectangle.setOpacity(0.2);
                ghostRectangles[i][j] = ghostRectangle;
                ghostBrickPanel.add(ghostRectangle, j, i);
            }
        }
        Point2D offset = getGamePanelOffset();
        double cellWidth = BRICK_SIZE + gamePanel.getHgap();
        double cellHeight = BRICK_SIZE + gamePanel.getVgap();
        brickPanel.setTranslateX(offset.getX() + brick.getxPosition() * cellWidth);
        brickPanel.setTranslateY(offset.getY() + (brick.getyPosition() - 2) * cellHeight);
        ghostBrickPanel.setTranslateX(offset.getX() + brick.getxPosition() * cellWidth);
        ghostBrickPanel.setTranslateY(offset.getY() + (brick.getGhostYPosition() - 2) * cellHeight);

        refreshNextBricks(brick.getNextThreeBricks());
        refreshHoldBrick(brick.getHoldBrickData());

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
            case 8:
                returnPaint = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.ORANGE), new Stop(1, Color.DARKRED));
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    private void refreshNextBrickPanel(GridPane panel, int[][] nextBrickData, int[][] storedData) {
        if (panel == null) return;
        if (nextBrickData == null) {
            panel.getChildren().clear();
            return;
        }

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

    private void refreshHoldBrick(int[][] holdBrickData) {
        if (holdBrickPanel == null) return;
        holdBrickPanel.getChildren().clear();
        if (holdBrickData != null) {
            for (int i = 0; i < holdBrickData.length; i++) {
                for (int j = 0; j < holdBrickData[i].length; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(getFillColor(holdBrickData[i][j]));
                    rectangle.setArcHeight(9);
                    rectangle.setArcWidth(9);
                    holdBrickPanel.add(rectangle, j, i);
                }
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

    public void refreshBrick(ViewData brick) {
        if (!isPause.getValue()) {
            brickPanel.toFront();
            ghostBrickPanel.toFront();
            refreshNextBricks(brick.getNextThreeBricks());
            refreshHoldBrick(brick.getHoldBrickData());
            Point2D offset = getGamePanelOffset();
            double cellWidth = BRICK_SIZE + gamePanel.getHgap();
            double cellHeight = BRICK_SIZE + gamePanel.getVgap();
            brickPanel.setTranslateX(offset.getX() + brick.getxPosition() * cellWidth);
            brickPanel.setTranslateY(offset.getY() + (brick.getyPosition() - 2) * cellHeight);
            ghostBrickPanel.setTranslateX(offset.getX() + brick.getxPosition() * cellWidth);
            ghostBrickPanel.setTranslateY(offset.getY() + (brick.getGhostYPosition() - 2) * cellHeight);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                    setGhostRectangleData(brick.getBrickData()[i][j], ghostRectangles[i][j]);
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

    private void setGhostRectangleData(int color, Rectangle rectangle) {
        if (color == 0) {
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setOpacity(0);
        } else {
            rectangle.setFill(getFillColor(color));
            rectangle.setArcHeight(9);
            rectangle.setArcWidth(9);
            rectangle.setOpacity(0.3); // Ghost brick is semi-transparent
        }
    }

    private void moveDown(MoveEvent event) {
        if (!isPause.getValue()){
            DownData downData = eventListener.onDownEvent(event);
            ClearRow clearRow = downData.getClearRow();
            if (clearRow != null) {
                if (clearRow.getLinesRemoved() > 0) {
                    NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.getScoreBonus());
                    notificationPane.getChildren().add(notificationPanel);
                    notificationPanel.showScore(notificationPane.getChildren());
                }
                refreshGameBackground(clearRow.getNewMatrix());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        if (scoreLabel != null && scoreProperty != null) {
            scoreLabel.textProperty().bind(scoreProperty.asString("%d"));
        }
    }

    public void bindHighScore(IntegerProperty highScoreProperty) {
        if (highScoreLabel != null && highScoreProperty != null) {
            highScoreLabel.textProperty().bind(highScoreProperty.asString("%d"));
        }
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        gameOverPanel.toFront();
        isGameOver.setValue(true);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        pauseMenuPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(false);
        isGameOver.setValue(false);
    }

    private void returnToMainMenu(ActionEvent event) {
        try {
            // Load the Main Menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
            Parent root = loader.load();

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new scene with the main menu
            Scene scene = new Scene(root, 800, 600);

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Could not load menu.fxml. Check your resources folder!");
        }
    }

    public void togglePause() {
        if (timeLine != null && !isGameOver.getValue()) {
            if (isPause.getValue()) {
                isPause.setValue(false);
                timeLine.play();
                if (pauseMenuPanel != null) {
                    pauseMenuPanel.setVisible(false);
                }
            } else {
                isPause.setValue(true);
                timeLine.pause();
                if (pauseMenuPanel != null) {
                    pauseMenuPanel.setVisible(true);
                    pauseMenuPanel.toFront();
                    notificationPane.toFront();
                }
            }
        }
        gamePanel.requestFocus();
    }

    public void pauseGame(ActionEvent actionEvent) {
        togglePause();
    }

    private Point2D getGamePanelOffset() {
        if (gameBoard == null) {
            return Point2D.ZERO;
        }

        return new Point2D(gameBoard.getLayoutX() + 12, gameBoard.getLayoutY() + 12);
    }

    public void showSpeedNotification() {
        if (speedNotificationLabel != null) {
            speedNotificationLabel.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(2000), speedNotificationLabel);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(event -> speedNotificationLabel.setVisible(false));
            ft.play();
        }
    }

    public void spawnFireEffect(double x, double y) {
        Point2D offset = getGamePanelOffset();
        double cellWidth = BRICK_SIZE + gamePanel.getHgap();
        double cellHeight = BRICK_SIZE + gamePanel.getVgap();
        double sceneX = offset.getX() + x * cellWidth + cellWidth / 2;
        double sceneY = offset.getY() + (y - 2) * cellHeight + cellHeight / 2;

        Random random = new Random();
        Color[] fireColors = {Color.RED, Color.ORANGE, Color.YELLOW};

        for (int i = 0; i < 30; i++) {
            Circle particle = new Circle(random.nextInt(5) + 2, fireColors[random.nextInt(fireColors.length)]);
            particle.setCenterX(sceneX);
            particle.setCenterY(sceneY);
            notificationPane.getChildren().add(particle);

            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = 40 + random.nextDouble() * 40;

            TranslateTransition tt = new TranslateTransition(Duration.millis(600 + random.nextInt(400)), particle);
            tt.setToX(sceneX + Math.cos(angle) * distance - sceneX);
            tt.setToY(sceneY + Math.sin(angle) * distance - sceneY);

            ScaleTransition st = new ScaleTransition(Duration.millis(tt.getDuration().toMillis()), particle);
            st.setToX(0);
            st.setToY(0);

            FadeTransition ft = new FadeTransition(Duration.millis(tt.getDuration().toMillis()), particle);
            ft.setToValue(0);

            ParallelTransition pt = new ParallelTransition(particle, tt, st, ft);
            pt.setOnFinished(e -> notificationPane.getChildren().remove(particle));
            pt.play();
        }
    }

    public void animateBlockRemoval(List<Point> clearedBlocks, Runnable onFinished) {
        if (clearedBlocks.isEmpty()) {
            onFinished.run();
            return;
        }

        List<Animation> animations = new ArrayList<>();
        for (Point p : clearedBlocks) {
            if (p.y >= 2 && p.y < displayMatrix.length && p.x >= 0 && p.x < displayMatrix[0].length) {
                Rectangle rect = displayMatrix[p.y][p.x];

                FadeTransition ft = new FadeTransition(Duration.millis(500), rect);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);

                ScaleTransition st = new ScaleTransition(Duration.millis(500), rect);
                st.setFromX(1.0);
                st.setFromY(1.0);
                st.setToX(0.0);
                st.setToY(0.0);

                ParallelTransition pt = new ParallelTransition(rect, ft, st);
                animations.add(pt);
            }
        }

        ParallelTransition allAnimations = new ParallelTransition(animations.toArray(new Animation[0]));
        allAnimations.setOnFinished(e -> {
            // Reset visual state after animation
            for (Point p : clearedBlocks) {
                if (p.y >= 2 && p.y < displayMatrix.length && p.x >= 0 && p.x < displayMatrix[0].length) {
                    Rectangle rect = displayMatrix[p.y][p.x];
                    rect.setScaleX(1.0);
                    rect.setScaleY(1.0);
                    rect.setOpacity(1.0);
                }
            }
            onFinished.run();
        });
        allAnimations.play();
    }

    public void updatePowerUpProgressBar(double progress) {
        if (powerUpProgressBar != null) {
            powerUpProgressBar.setProgress(progress);
        }
    }

    public void setPowerUpContainerVisibility(boolean isVisible) {
        if (powerUpContainer != null) {
            powerUpContainer.setVisible(isVisible);
            powerUpContainer.setManaged(isVisible);
        }
    }
}