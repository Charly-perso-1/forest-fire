package com.forestfire.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record SimulationParameters(int width, int height, Set<Position> initialBurningCells, double spreadProbability,
                                   int simulationStepDelayMs) {

    public SimulationParameters(int width, int height, Set<Position> initialBurningCells, double spreadProbability) {
        this(width, height, initialBurningCells, spreadProbability, 600);
    }

    public SimulationParameters {
        if (width <= 0) {
            throw new IllegalArgumentException("Grid width must be positive.");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Grid height must be positive.");
        }
        if (spreadProbability < 0.0 || spreadProbability > 1.0) {
            throw new IllegalArgumentException("Spread probability must be between 0.0 and 1.0.");
        }
        if (simulationStepDelayMs <= 0) {
            throw new IllegalArgumentException("Simulation step delay must be positive.");
        }
        initialBurningCells = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(initialBurningCells)));
        for (Position cell : initialBurningCells) {
            if (cell.row() < 0 || cell.row() >= height || cell.column() < 0 || cell.column() >= width) {
                throw new IllegalArgumentException(
                        "Initial burning cell out of bounds: " + cell + " for grid " + width + "x" + height);
            }
        }
    }

    public static SimulationParameters fromProperties(Properties properties) {
        int width = parsePositiveInteger(properties, "app.grid-width");
        int height = parsePositiveInteger(properties, "app.grid-height");
        double spreadProbability = parseProbability(properties, "app.spread-probability");
        int simulationStepDelayMs = parsePositiveInteger(properties, "app.simulation-step-delay-ms", 600);
        Set<Position> initialBurningCells = parseBurningCells(properties.getProperty("app.begin-burning-cells"));
        return new SimulationParameters(width, height, initialBurningCells, spreadProbability, simulationStepDelayMs);
    }

    private static int parsePositiveInteger(Properties properties, String key) {
        return parsePositiveInteger(properties, key, null);
    }

    private static int parsePositiveInteger(Properties properties, String key, Integer defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw new IllegalArgumentException("Missing property: " + key);
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                throw new IllegalArgumentException("Property " + key + " must be greater than zero.");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid integer value for " + key + ": " + value, ex);
        }
    }

    private static double parseProbability(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing property: " + key);
        }
        try {
            double parsed = Double.parseDouble(value.trim());
            if (parsed < 0.0 || parsed > 1.0) {
                throw new IllegalArgumentException("Property " + key + " must be between 0.0 and 1.0.");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid probability for " + key + ": " + value, ex);
        }
    }

    private static Set<Position> parseBurningCells(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return Set.of();
        }

        return Stream.of(rawValue.split("[;\\s]+"))
                .filter(token -> !token.isBlank())
                .map(token -> token.replace("(", "").replace(")", ""))
                .filter(token -> token.contains(","))
                .map(token -> {
                    String[] coordinates = token.split(",");
                    if (coordinates.length != 2) {
                        throw new IllegalArgumentException("Invalid burning cell definition: " + token + ". Use row,column.");
                    }
                    int row = Integer.parseInt(coordinates[0].trim());
                    int column = Integer.parseInt(coordinates[1].trim());
                    return new Position(row, column);
                })
                .collect(Collectors.toSet());
    }
}
