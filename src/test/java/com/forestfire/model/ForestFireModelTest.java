package com.forestfire.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ForestFireModelTest {

    @Test
    void modelInitializesBurningCells() {
        SimulationParameters parameters = new SimulationParameters(4, 4, Set.of(new Position(1, 1), new Position(2, 3)), 0.5);

        ForestFireModel model = new ForestFireModel(parameters);

        assertEquals(CellState.BURNING, model.getCellState(1, 1));
        assertEquals(CellState.BURNING, model.getCellState(2, 3));
        assertEquals(CellState.LIVING, model.getCellState(0, 0));
    }

    @Test
    void fireExtinguishesAndSpreadsToNeighbors() {
        SimulationParameters parameters = new SimulationParameters(3, 3, Set.of(new Position(1, 1)), 1.0);
        ForestFireModel model = new ForestFireModel(parameters);

        model.step();

        assertEquals(CellState.ASH, model.getCellState(1, 1));
        assertEquals(CellState.BURNING, model.getCellState(0, 1));
        assertEquals(CellState.BURNING, model.getCellState(1, 0));
        assertEquals(CellState.BURNING, model.getCellState(1, 2));
        assertEquals(CellState.BURNING, model.getCellState(2, 1));
    }

    @Test
    void modelStopsWhenThereIsNoFireLeft() {
        SimulationParameters parameters = new SimulationParameters(2, 2, Set.of(new Position(0, 0)), 0.0);
        ForestFireModel model = new ForestFireModel(parameters);

        model.step();

        assertTrue(model.isComplete());
        assertEquals(CellState.ASH, model.getCellState(0, 0));
    }
}
