package com.comp2042.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main entry point for the Tetris application.
 * It extends the {@link Application} class from JavaFX and sets up the primary stage.
 */
public class Main extends Application {

    /**
     * Initializes and displays the primary stage of the application.
     * It loads the main menu from an FXML file, sets the title, and shows the scene.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        if (primaryStage == null) {
            primaryStage = new Stage();
        }

        primaryStage.setTitle("TETRIS");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Menu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
