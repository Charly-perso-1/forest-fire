package com.forestfire.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ForestFireModel {
    private final int width;
    private final int height;
    private final double spreadProbability;
    private CellState[][] grid;
    private int elapsedSteps;

    public ForestFireModel(SimulationParameters parameters) {
        this.width = parameters.width();
        this.height = parameters.height();
        this.spreadProbability = parameters.spreadProbability();
        this.grid = new CellState[height][width];

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                grid[row][column] = CellState.LIVING;
            }
        }

        for (Position position : parameters.initialBurningCells()) {
            grid[position.row()][position.column()] = CellState.BURNING;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getElapsedSteps() {
        return elapsedSteps;
    }

    public CellState getCellState(int row, int column) {
        validateCoordinates(row, column);
        return grid[row][column];
    }

    public boolean hasBurningCells() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (grid[row][column] == CellState.BURNING) {
                    return true;
                }
            }
        }
        return false;
    }

    public void step() {
        if (!hasBurningCells()) {
            return;
        }

        CellState[][] nextGrid = new CellState[height][width];
        Set<Position> burningCells = new HashSet<>(findBurningCells());

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                CellState currentState = grid[row][column];
                if (currentState == CellState.BURNING) {
                    nextGrid[row][column] = CellState.ASH;
                    continue;
                }
                if (currentState == CellState.ASH) {
                    nextGrid[row][column] = CellState.ASH;
                    continue;
                }

                boolean shouldIgnite = false;
                for (Position neighbor : findNeighbors(row, column)) {
                    if (burningCells.contains(neighbor) && Math.random() < spreadProbability) {
                        shouldIgnite = true;
                        break;
                    }
                }

                nextGrid[row][column] = shouldIgnite ? CellState.BURNING : CellState.LIVING;
            }
        }

        grid = nextGrid;
        elapsedSteps++;
    }

    public List<Position> findBurningCells() {
        List<Position> burningCells = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (grid[row][column] == CellState.BURNING) {
                    burningCells.add(new Position(row, column));
                }
            }
        }
        return burningCells;
    }

    public boolean isComplete() {
        return !hasBurningCells();
    }

    public CellState[][] copyGrid() {
        CellState[][] copy = new CellState[height][];
        for (int row = 0; row < height; row++) {
            copy[row] = new CellState[width];
            System.arraycopy(grid[row], 0, copy[row], 0, width);
        }
        return copy;
    }

    private Set<Position> findNeighbors(int row, int column) {
        Set<Position> neighbors = new HashSet<>();
        int[] rowOffsets = {-1, 0, 1, 0};
        int[] columnOffsets = {0, 1, 0, -1};

        for (int index = 0; index < rowOffsets.length; index++) {
            int nextRow = row + rowOffsets[index];
            int nextColumn = column + columnOffsets[index];
            if (nextRow >= 0 && nextRow < height && nextColumn >= 0 && nextColumn < width) {
                neighbors.add(new Position(nextRow, nextColumn));
            }
        }
        return neighbors;
    }

    private void validateCoordinates(int row, int column) {
        if (row < 0 || row >= height || column < 0 || column >= width) {
            throw new IllegalArgumentException("Cell coordinates out of bounds: row=" + row + ", column=" + column);
        }
    }
}
