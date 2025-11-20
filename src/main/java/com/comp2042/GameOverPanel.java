package com.comp2042;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class GameOverPanel extends BorderPane {

    private Button newGameButton;

    public GameOverPanel() {
        VBox container = new VBox(15);
        container.setAlignment(javafx.geometry.Pos.CENTER);

        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        newGameButton = new Button("New Game");
        newGameButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 120px; -fx-pref-height: 35px;");

        container.getChildren().addAll(gameOverLabel, newGameButton);
        setCenter(container);
    }

    // Wires button click to handler
    public void setOnNewGameButtonClick(EventHandler<ActionEvent> handler) {
        newGameButton.setOnAction(handler);
    }

}
