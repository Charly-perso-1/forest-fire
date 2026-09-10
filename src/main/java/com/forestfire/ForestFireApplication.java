package com.forestfire;

import com.forestfire.config.ConfigLoader;
import com.forestfire.controller.SimulationController;
import com.forestfire.model.ForestFireModel;
import com.forestfire.model.SimulationParameters;
import com.forestfire.view.ForestFireSceneBuilder;
import com.forestfire.view.ForestFireView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
            ForestFireSceneBuilder sceneBuilder = new ForestFireSceneBuilder(view);
            controller = new SimulationController(model, view, parameters.simulationStepDelayMs());
            stage.setScene(new Scene(sceneBuilder.buildScene(controller), 820, 700));
            controller.initialize();
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
