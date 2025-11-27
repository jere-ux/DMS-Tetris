package com.comp2042;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class menuController {

    public ImageView backgroundImage;
    @FXML private Pane particlePane;
    @FXML private Button newGameBtn;
    @FXML private Button optionsBtn;
    @FXML private Button quitBtn;

    private final List<FallingShape> shapes = new ArrayList<>();
    private final Random random = new Random();

    // Cyberpunk Neon Colors
    private final Color[] NEON_COLORS = {
            Color.web("#39ff14"), // Green
            Color.web("#00f0ff"), // Cyan
            Color.web("#ff003c"), // Red
            Color.web("#fff01f"), // Yellow
            Color.web("#b026ff")  // Purple
    };

    @FXML
    public void initialize() {
        startBackgroundAnimation();
    }

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

    private void spawnTetromino() {
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

    @FXML
    public void onNewGame(ActionEvent event) {
        try {
            // Load the Game Layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
            Parent root = loader.load();

            // Initialize the Game Logic
            GuiController guiController = loader.getController();
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

    @FXML
    public void onOptions(ActionEvent event) {
        System.out.println("Options clicked");
    }

    @FXML
    public void onQuit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public Button getNewGameBtn() {
        return newGameBtn;
    }

    public void setNewGameBtn(Button newGameBtn) {
        this.newGameBtn = newGameBtn;
    }

    public Button getOptionsBtn() {
        return optionsBtn;
    }

    public void setOptionsBtn(Button optionsBtn) {
        this.optionsBtn = optionsBtn;
    }

    public Button getQuitBtn() {
        return quitBtn;
    }

    public void setQuitBtn(Button quitBtn) {
        this.quitBtn = quitBtn;
    }

    public void onSettings(ActionEvent actionEvent) {
    }

    private static class FallingShape {
        Shape shape;
        double speed;

        public FallingShape(Shape shape, double speed) {
            this.shape = shape;
            this.speed = speed;
        }
    }
}