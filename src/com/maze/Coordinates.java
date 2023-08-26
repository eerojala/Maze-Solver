package com.maze;

/**
 * Wrapper for coordinates so don't have to use verbose AbstractMap.SimpleEntry.
 */
public class Coordinates {
    private final int y;
    private final int x;

    public Coordinates(int y, int x) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
