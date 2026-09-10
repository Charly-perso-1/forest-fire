package com.forestfire.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record SimulationParameters(int width, int height, Set<Position> initialBurningCells, double spreadProbability) {

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
        Set<Position> initialBurningCells = parseBurningCells(properties.getProperty("app.begin-burning-cells"), width, height);
        return new SimulationParameters(width, height, initialBurningCells, spreadProbability);
    }

    private static int parsePositiveInteger(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
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

    private static Set<Position> parseBurningCells(String rawValue, int width, int height) {
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
