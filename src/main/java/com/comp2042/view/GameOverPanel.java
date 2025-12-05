package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class GameOverPanel extends BorderPane {

    private Button newGameButton;
    private Button mainMenuButton;

    public GameOverPanel() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);

        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        newGameButton = new Button("New Game");
        newGameButton.getStyleClass().addAll("cyber-button", "green-btn");

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().addAll("cyber-button", "blue-btn");

        container.getChildren().addAll(gameOverLabel, newGameButton, mainMenuButton);
        setCenter(container);
        BorderPane.setAlignment(container, Pos.CENTER);
    }

    // Wires button click to handler
    public void setOnNewGameButtonClick(EventHandler<ActionEvent> handler) {
        newGameButton.setOnAction(handler);
    }

    public void setOnMainMenuButtonClick(EventHandler<ActionEvent> handler) {
        mainMenuButton.setOnAction(handler);
    }

}