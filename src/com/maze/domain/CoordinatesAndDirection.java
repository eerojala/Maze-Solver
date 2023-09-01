package com.maze.domain;

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
