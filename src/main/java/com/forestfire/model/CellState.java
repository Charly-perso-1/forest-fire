package com.forestfire.model;

public enum CellState {
    LIVING,
    BURNING,
    ASH;

    public boolean isBurning() {
        return this == BURNING;
    }
}
