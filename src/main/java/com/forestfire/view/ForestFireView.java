package com.forestfire.view;

import com.forestfire.model.CellState;
import com.forestfire.model.ForestFireModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ForestFireView {
    private final GridPane gridPane;
    private final Label statusLabel;

    public ForestFireView() {
        gridPane = new GridPane();
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        gridPane.setPadding(new Insets(10));

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    }

    public void render(ForestFireModel model) {
        gridPane.getChildren().clear();

        for (int row = 0; row < model.getHeight(); row++) {
            for (int column = 0; column < model.getWidth(); column++) {
                Rectangle cell = new Rectangle(32, 32);
                cell.setArcWidth(6);
                cell.setArcHeight(6);
                cell.setStroke(Color.web("#1f2937"));
                cell.setStrokeWidth(1.0);
                cell.setFill(mapState(model.getCellState(row, column)));
                GridPane.setRowIndex(cell, row);
                GridPane.setColumnIndex(cell, column);
                gridPane.getChildren().add(cell);
            }
        }

        if (model.isComplete()) {
            statusLabel.setText("Simulation finished — no fire remains.");
            statusLabel.setTextFill(Color.DARKGREEN);
        } else {
            statusLabel.setText("Simulation running — step " + model.getElapsedSteps());
            statusLabel.setTextFill(Color.DARKRED);
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    private Color mapState(CellState state) {
        return switch (state) {
            case LIVING -> Color.DARKGREEN;
            case BURNING -> Color.DARKRED;
            case ASH -> Color.GRAY;
        };
    }
}
