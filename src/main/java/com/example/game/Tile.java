package com.example.game;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {

    private Rectangle tile;
    private Label label;
    private String labelText;


    public Tile(double width, double height, String labelText, String currentMovieLabel) {
        tile = new Rectangle(width, height);
        this.labelText = labelText;
        tile.setFill(Color.RED);
        tile.setStroke(Color.YELLOW);

        label = new Label();
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 15px;-fx-font-weight: bold ; -fx-text-fill : white ;");
        label.setAlignment(Pos.CENTER);
        label.setText(labelText);


        setAlignment(Pos.CENTER);
        getChildren().addAll(tile, label);
        setTileColor(currentMovieLabel);
    }

    public void setTileColor(String movie) {
        if (movie.equalsIgnoreCase(labelText)) {
            tile.setFill(Color.GREEN);

        }
    }


}
