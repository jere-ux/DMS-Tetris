package com.comp2042;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class PauseMenuPanel extends BorderPane {

    private Button resumeButton;
    private Button newGameButton;

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

    // Wires resume button click to handler
    public void setOnResumeButtonClick(EventHandler<ActionEvent> handler) {
        resumeButton.setOnAction(handler);
    }

    // Wires new game button click to handler
    public void setOnNewGameButtonClick(EventHandler<ActionEvent> handler) {
        newGameButton.setOnAction(handler);
    }

}
