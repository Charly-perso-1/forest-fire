package com.forestfire.view;

import com.forestfire.controller.SimulationController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ForestFireSceneBuilder {
    private final ForestFireView view;

    public ForestFireSceneBuilder(ForestFireView view) {
        this.view = view;
    }

    public BorderPane buildScene(SimulationController controller) {
        BorderPane root = new BorderPane();
        root.setCenter(view.getGridPane());

        Label title = new Label("Forest Fire Simulation");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button stepButton = new Button("Next step");
        stepButton.setOnAction(event -> controller.stepOnce());

        Button autoButton = new Button("Run automatically");
        autoButton.setOnAction(event -> controller.startAutomaticSimulation());

        VBox controls = new VBox(12, title, view.getStatusLabel(), stepButton, autoButton);
        controls.setAlignment(Pos.CENTER);
        controls.setStyle("-fx-padding: 12; -fx-background-color: #f8fafc;");
        root.setTop(controls);
        return root;
    }
}
