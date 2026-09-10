package com.forestfire;

import com.forestfire.config.ConfigLoader;
import com.forestfire.controller.SimulationController;
import com.forestfire.model.ForestFireModel;
import com.forestfire.model.SimulationParameters;
import com.forestfire.view.ForestFireView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForestFireApplication extends Application {
    private SimulationController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            SimulationParameters parameters = ConfigLoader.load();
            ForestFireModel model = new ForestFireModel(parameters);
            ForestFireView view = new ForestFireView();
            controller = new SimulationController(model, view);

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

            controller.initialize();
            stage.setScene(new Scene(root, 820, 700));
            stage.setTitle("Forest fire propagation");
            stage.show();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Configuration error");
            alert.setHeaderText("Unable to load the simulation configuration");
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
            System.exit(1);
        }
    }
}
