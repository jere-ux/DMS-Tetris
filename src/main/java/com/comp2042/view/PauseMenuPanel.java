package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Represents the pause menu screen of the Tetris application.
 * This panel is displayed when the game is paused, providing options to resume or start a new game.
 */
public class PauseMenuPanel extends BorderPane {

    private Button resumeButton;
    private Button newGameButton;

    /**
     * Constructs a new PauseMenuPanel.
     * It initializes the UI components, such as labels and buttons,
     * and arranges them in the center of the panel.
     */
    public PauseMenuPanel() {
        VBox container = new VBox(15);
        container.setAlignment(javafx.geometry.Pos.CENTER);
        container.getStyleClass().add("help-pane");
        container.setMaxSize(300, 300);
        getStylesheets().add(getClass().getResource("/menu-style.css").toExternalForm());

        final Label pauseLabel = new Label("PAUSED");
        pauseLabel.getStyleClass().add("pause-label");

        resumeButton = new Button("Resume");
        resumeButton.getStyleClass().addAll("cyber-button", "yellow-btn");

        newGameButton = new Button("New Game");
        newGameButton.getStyleClass().addAll("cyber-button", "green-btn");

        container.getChildren().addAll(pauseLabel, resumeButton, newGameButton);
        setCenter(container);

        // Semi-transparent dark background
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
    }

    /**
     * Sets the event handler for the "Resume" button click.
     *
     * @param handler The event handler to be executed when the button is clicked.
     */
    public void setOnResumeButtonClick(EventHandler<ActionEvent> handler) {
        resumeButton.setOnAction(handler);
    }

    /**
     * Sets the event handler for the "New Game" button click.
     *
     * @param handler The event handler to be executed when the button is clicked.
     */
    public void setOnNewGameButtonClick(EventHandler<ActionEvent> handler) {
        newGameButton.setOnAction(handler);
    }

}
