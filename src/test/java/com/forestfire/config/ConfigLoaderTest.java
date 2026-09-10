package com.forestfire.config;

import com.forestfire.model.Position;
import com.forestfire.model.SimulationParameters;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigLoaderTest {

    @Test
    void fromPropertiesParsesGridDimensionsAndBurningCells() {
        Properties properties = new Properties();
        properties.setProperty("app.grid-width", "7");
        properties.setProperty("app.grid-height", "5");
        properties.setProperty("app.begin-burning-cells", "0,0;2,3");
        properties.setProperty("app.spread-probability", "0.75");
        properties.setProperty("app.simulation-step-delay-ms", "250");

        SimulationParameters parameters = SimulationParameters.fromProperties(properties);

        assertEquals(7, parameters.width());
        assertEquals(5, parameters.height());
        assertEquals(0.75, parameters.spreadProbability());
        assertEquals(250, parameters.simulationStepDelayMs());
        assertEquals(2, parameters.initialBurningCells().size());
        assertTrue(parameters.initialBurningCells().contains(new Position(0, 0)));
        assertTrue(parameters.initialBurningCells().contains(new Position(2, 3)));
    }

    @Test
    void loadReadsProjectFile() throws IOException {
        SimulationParameters parameters = ConfigLoader.load();

        assertEquals(10, parameters.width());
        assertEquals(10, parameters.height());
        assertEquals(0.6, parameters.spreadProbability());
        assertEquals(600, parameters.simulationStepDelayMs());
        assertEquals(3, parameters.initialBurningCells().size());
    }
}
