package com.comp2042.controller;

import com.comp2042.model.GameLevel;
import com.comp2042.model.LeaderboardManager;
import com.comp2042.model.ScoreEntry;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Controller for the main menu of the game.
 * Handles menu navigation, background animations, music, and level selection.
 */
public class MenuController {

    @FXML private ImageView backgroundImage;
    @FXML private Button helpBtn;
    @FXML private Pane particlePane;
    @FXML private VBox mainMenuVBox;
    @FXML private VBox helpPane;
    @FXML private VBox levelSelectionVBox;
    @FXML private Button playButton;
    @FXML private Button quitButton;
    @FXML private Button leaderboardButton;
    @FXML private VBox leaderboardPane;
    @FXML private VBox leaderboardScoresVBox;

    @FXML private Button levelAButton;
    @FXML private Button levelBButton;
    @FXML private Button levelCButton;
    @FXML private Label selectedLevelLabel;

    private static volatile GameLevel.LevelType selectedLevelType = GameLevel.LevelType.TYPE_A_SPEED_CURVE;

    private final List<FallingShape> shapes = new ArrayList<>();
    private final Random random = new Random();
    private MediaPlayer mediaPlayer; // For menu music
    private LeaderboardManager leaderboardManager;


    // Cyberpunk Neon Colors
    private final Color[] NEON_COLORS = {
            Color.web("#39ff14"), // Green
            Color.web("#00f0ff"), // Cyan
            Color.web("#ff003c"), // Red
            Color.web("#fff01f"), // Yellow
            Color.web("#b026ff")  // Purple
    };

    /**
     * Initializes the menu controller.
     */
    @FXML
    public void initialize() {
        startBackgroundAnimation();
        playMenuMusic();
        leaderboardManager = new LeaderboardManager();
        leaderboardManager.loadScores();
    }

    /**
     * Plays the background music on a loop.
     */
    private void playMenuMusic() {
        try {
            String musicFile = "/menu.mp3"; // Music file in resources
            Media sound = new Media(getClass().getResource(musicFile).toString());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop music
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error: Could not play menu music. Make sure 'menu.mp3' is in the resources folder.");
            e.printStackTrace();
        }
    }

    /**
     * Stops the menu music.
     */
    private void stopMenuMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Starts the background animation of falling tetrominoes.
     */
    private void startBackgroundAnimation() {

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // Spawn a new shape every 0.8 seconds
                if (now - lastUpdate >= 800_000_000) {
                    spawnTetromino();
                    lastUpdate = now;
                }
                updateShapes();
            }
        };
        timer.start();
    }

    /**
     * Spawns a new random tetromino shape for the background animation.
     */
    private void spawnTetromino() {
        if (particlePane.getWidth() <= 0) {
            return; // Don't spawn if the pane isn't ready
        }
        Polygon tetromino = createRandomTetromino();


        tetromino.setTranslateX(random.nextInt((int) particlePane.getWidth()));
        tetromino.setTranslateY(-100); // Start off-screen


        tetromino.setFill(Color.TRANSPARENT);
        tetromino.setStroke(NEON_COLORS[random.nextInt(NEON_COLORS.length)]);
        tetromino.setStrokeWidth(3);


        Glow glow = new Glow();
        glow.setLevel(0.9);
        tetromino.setEffect(glow);

        // Rotation
        tetromino.setRotate(random.nextInt(360));

        particlePane.getChildren().add(tetromino);
        shapes.add(new FallingShape(tetromino, 1 + random.nextDouble() * 2));
    }

    /**
     * Creates a random tetromino shape as a Polygon.
     * @return A Polygon representing a random tetromino.
     */
    private Polygon createRandomTetromino() {
        int type = random.nextInt(5);
        double s = 30; // Size of one block unit

        return switch (type) {
            case 0 -> // I Shape
                    new Polygon(0, 0, s, 0, s, 4 * s, 0, 4 * s);
            case 1 -> // O Shape
                    new Polygon(0, 0, 2 * s, 0, 2 * s, 2 * s, 0, 2 * s);
            case 2 -> // T Shape
                    new Polygon(0, 0, 3 * s, 0, 3 * s, s, 2 * s, s, 2 * s, 2 * s, s, 2 * s, s, s, 0, s);
            case 3 -> // L Shape
                    new Polygon(0, 0, s, 0, s, 2 * s, 2 * s, 2 * s, 2 * s, 3 * s, 0, 3 * s);
            case 4 -> // Z Shape
                    new Polygon(0, 0, 2 * s, 0, 2 * s, s, 3 * s, s, 3 * s, 2 * s, s, 2 * s, s, s, 0, s);
            default -> new Polygon(0, 0, s, 0, s, s, 0, s);
        };
    }

    /**
     * Updates the position and rotation of the falling shapes in the background.
     */
    private void updateShapes() {
        Iterator<FallingShape> iter = shapes.iterator();
        while (iter.hasNext()) {
            FallingShape fb = iter.next();
            Shape shape = fb.shape;

            // Move down
            shape.setTranslateY(shape.getTranslateY() + fb.speed);
            shape.setRotate(shape.getRotate() + 0.5); // Spin


            if (shape.getTranslateY() > particlePane.getHeight()) {
                particlePane.getChildren().remove(shape);
                iter.remove();
            }
        }
    }

    /**
     * Handles the selection of level A.
     * @param event The action event.
     */
    @FXML
    private void onLevelASelected(ActionEvent event) {
        selectedLevelType = GameLevel.LevelType.TYPE_A_SPEED_CURVE;
        onNewGame(event);
    }

    /**
     * Handles the selection of level B.
     * @param event The action event.
     */
    @FXML
    private void onLevelBSelected(ActionEvent event) {
        selectedLevelType = GameLevel.LevelType.TYPE_B_NORMAL;
        onNewGame(event);
    }

    /**
     * Handles the selection of level C.
     * @param event The action event.
     */
    @FXML
    private void onLevelCSelected(ActionEvent event) {
        selectedLevelType = GameLevel.LevelType.TYPE_C_OBSTACLES;
        onNewGame(event);
    }

    /**
     * Gets the currently selected game level type.
     * @return The selected game level type.
     */
    public static GameLevel.LevelType getSelectedLevelType() {
        return selectedLevelType;
    }

    /**
     * Starts a new game.
     * @param event The action event.
     */
    private void onNewGame(ActionEvent event) {
        stopMenuMusic(); // Stop music before starting game
        try {
            // Load the Game Layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
            Parent root = loader.load();

            // Initialize the Game Logic
            com.comp2042.view.GuiController guiController = loader.getController();
            new GameController(guiController);

            // Switch the Scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Could not load gameLayout.fxml. Check your resources folder!");
        }
    }

    /**
     * Quits the application.
     * @param event The action event.
     */
    @FXML
    public void onQuit(ActionEvent event) {
        stopMenuMusic(); // Stop music before quitting
        Platform.exit();
        System.exit(0);
    }

    /**
     * Shows the help screen.
     * @param actionEvent The action event.
     */
    @FXML
    public void onHelp(ActionEvent actionEvent) {
        mainMenuVBox.setVisible(false);
        helpPane.setVisible(true);
    }

    /**
     * Returns to the main menu from the help screen.
     * @param actionEvent The action event.
     */
    @FXML
    public void onHelpBack(ActionEvent actionEvent) {
        helpPane.setVisible(false);
        mainMenuVBox.setVisible(true);
    }

    /**
     * Shows the level selection screen.
     * @param event The action event.
     */
    @FXML
    private void onPlay(ActionEvent event) {
        levelSelectionVBox.setVisible(!levelSelectionVBox.isVisible());
    }

    /**
     * Shows the leaderboard screen.
     * @param event The action event.
     */
    @FXML
    private void onLeaderboard(ActionEvent event) {
        mainMenuVBox.setVisible(false);
        leaderboardPane.setVisible(true);
        onLeaderboardModeA(event); // Default to showing mode A scores
    }

    /**
     * Returns to the main menu from the leaderboard screen.
     * @param event The action event.
     */
    @FXML
    private void onLeaderboardBack(ActionEvent event) {
        leaderboardPane.setVisible(false);
        mainMenuVBox.setVisible(true);
    }

    /**
     * Shows the leaderboard for level A.
     * @param event The action event.
     */
    @FXML
    private void onLeaderboardModeA(ActionEvent event) {
        updateLeaderboardView(GameLevel.LevelType.TYPE_A_SPEED_CURVE);
    }

    /**
     * Shows the leaderboard for level B.
     * @param event The action event.
     */
    @FXML
    private void onLeaderboardModeB(ActionEvent event) {
        updateLeaderboardView(GameLevel.LevelType.TYPE_B_NORMAL);
    }

    /**
     * Shows the leaderboard for level C.
     * @param event The action event.
     */
    @FXML
    private void onLeaderboardModeC(ActionEvent event) {
        updateLeaderboardView(GameLevel.LevelType.TYPE_C_OBSTACLES);
    }

    /**
     * Updates the leaderboard view with scores for the specified level type.
     * @param levelType The level type to display scores for.
     */
    private void updateLeaderboardView(GameLevel.LevelType levelType) {
        leaderboardScoresVBox.getChildren().clear();
        List<ScoreEntry> scores = leaderboardManager.getScores(levelType);
        int rank = 1;
        for (ScoreEntry score : scores) {
            if (rank > 5) {
                break;
            }
            Label scoreLabel = new Label(rank + ". " + score.getName() + " - " + score.getScore());
            scoreLabel.getStyleClass().add("leaderboard-scores");
            leaderboardScoresVBox.getChildren().add(scoreLabel);
            rank++;
        }
    }

    /**
     * A helper class to represent a falling shape in the background animation.
     */
    private static class FallingShape {
        private final Shape shape;
        private final double speed;

        public FallingShape(Shape shape, double speed) {
            this.shape = shape;
            this.speed = speed;
        }
    }
}
