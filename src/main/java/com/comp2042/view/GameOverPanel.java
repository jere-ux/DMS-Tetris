package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


/**
 * Represents the "Game Over" screen of the Tetris application.
 * This panel is displayed when the game ends, showing the "GAME OVER" message
 * and providing options to start a new game, return to the main menu, or submit a score.
 */
public class GameOverPanel extends BorderPane {

    private Button newGameButton;
    private Button mainMenuButton;
    private Button submitButton;
    private TextField nameField;

    /**
     * Constructs a new GameOverPanel.
     * It initializes the UI components, such as labels, buttons, and a text field for the player's name,
     * and arranges them in the center of the panel.
     */
    public GameOverPanel() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);

        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.getStyleClass().add("cyber-textfield");
        nameField.setMaxWidth(200);

        submitButton = new Button("Submit Score");
        submitButton.getStyleClass().addAll("cyber-button", "green-btn");

        newGameButton = new Button("New Game");
        newGameButton.getStyleClass().addAll("cyber-button", "green-btn");

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().addAll("cyber-button", "blue-btn");

        container.getChildren().addAll(gameOverLabel, nameField, submitButton, newGameButton, mainMenuButton);
        setCenter(container);
        BorderPane.setAlignment(container, Pos.CENTER);
    }

    /**
     * Gets the name entered by the player in the text field.
     *
     * @return The player's name as a String.
     */
    public String getPlayerName() {
        return nameField.getText();
    }

    /**
     * Sets the event handler for the "New Game" button click.
     *
     * @param handler The event handler to be executed when the button is clicked.
     */
    public void setOnNewGameButtonClick(EventHandler<ActionEvent> handler) {
        newGameButton.setOnAction(handler);
    }

    /**
     * Sets the event handler for the "Main Menu" button click.
     *
     * @param handler The event handler to be executed when the button is clicked.
     */
    public void setOnMainMenuButtonClick(EventHandler<ActionEvent> handler) {
        mainMenuButton.setOnAction(handler);
    }

    /**
     * Sets the event handler for the "Submit Score" button click.
     *
     * @param handler The event handler to be executed when the button is clicked.
     */
    public void setOnSubmitScoreButtonClick(EventHandler<ActionEvent> handler) {
        submitButton.setOnAction(handler);
    }
}