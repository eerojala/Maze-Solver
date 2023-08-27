package com.maze.domain;

/**
 * Wrapper for coordinates so don't have to use verbose AbstractMap.SimpleEntry.
 */
public class Coordinates {
    private final int y;
    private final int x;

    /**
     * Creates a new coordinate objects. Note that y is given before x.
     * @param y
     * @param x
     */
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
