package com.forestfire.controller;

import com.forestfire.model.ForestFireModel;
import com.forestfire.view.ForestFireView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SimulationController {
    private final ForestFireModel model;
    private final ForestFireView view;
    private final int simulationStepDelayMs;
    private Timeline simulationTimeline;

    public SimulationController(ForestFireModel model, ForestFireView view, int simulationStepDelayMs) {
        this.model = model;
        this.view = view;
        this.simulationStepDelayMs = simulationStepDelayMs;
    }

    public void initialize() {
        view.render(model);
    }

    public void stepOnce() {
        if (model.isComplete()) {
            stopAutomaticSimulation();
            view.render(model);
            return;
        }

        model.step();
        view.render(model);
    }

    public void startAutomaticSimulation() {
        if (simulationTimeline != null && simulationTimeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        simulationTimeline = new Timeline(new KeyFrame(Duration.millis(simulationStepDelayMs), event -> {
            if (model.isComplete()) {
                stopAutomaticSimulation();
                return;
            }
            model.step();
            view.render(model);
        }));
        simulationTimeline.setCycleCount(Animation.INDEFINITE);
        simulationTimeline.play();
    }

    public void stopAutomaticSimulation() {
        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }
    }

    public ForestFireModel getModel() {
        return model;
    }
}
