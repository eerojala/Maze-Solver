package com.maze.domain;

public enum Direction {
    UP('↑', -1, 0),
    RIGHT('→', 0, 1),
    DOWN('↓', 1, 0),
    LEFT('←', 0, -1),
    INITIAL('?', 0, 0); // For the start of maze solving before any moves have been made

    private final char ch;
    private final int yAppend;
    private final int xAppend;

    Direction(char ch, int yAppend, int xAppend) {
        this.ch = ch;
        this.yAppend = yAppend;
        this.xAppend = xAppend;
    }

    public char getChar() {
        return ch;
    }

    public static Coordinates getNextCoordinates(Direction direction, Coordinates coordinates) {
        if (direction == null || direction == Direction.INITIAL) {
            throw new IllegalArgumentException("Cannot give next coordinates for null or INITIAL direction");
        }

        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot give next coordinates for null coordinates");
        }

        int y = coordinates.getY() + direction.yAppend;
        int x = coordinates.getX() + direction.xAppend;

        return new Coordinates(y, x);
    }
    public static final Direction[] MOVING_DIRECTIONS = new Direction[] { UP, RIGHT, DOWN, LEFT };
}
