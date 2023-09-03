package com.maze.domain;

/**
 * Wrapper for Coordinate and Direction pairs so don't have to use verbose AbstractMap.SimpleEntry.
 */
public class CoordinatesAndDirection {
    private final Coordinates coordinates;
    private final Direction direction;

    public CoordinatesAndDirection(Coordinates coordinates, Direction direction) {
        this.coordinates = coordinates;
        this.direction = direction;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Direction getDirection() {
        return direction;
    }
}
