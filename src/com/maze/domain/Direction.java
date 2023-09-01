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

    /**
     * Returns a char representation of the Direction.
     * UP = '↑'
     * RIGHT = '→'
     * DOWN = '↓'
     * LEFT = '←'
     * INITIAL = '?'
     * @return
     */
    public char getChar() {
        return ch;
    }

    /**
     * Returns coordinates next to the given coordinates from the given direction, e.g. if direction is UP, then returns
     * coordinates which are above.
     * @param direction not null or INITIAL
     * @param coordinates not null
     * @return
     */
    public static Coordinates getNextCoordinates(Direction direction, Coordinates coordinates) {
        return getNextCoordinates(direction, coordinates, 1);
    }

    public static Coordinates getPreviousCoordinates(Direction direction, Coordinates coordinates) {
        return getNextCoordinates(direction, coordinates, -1);
    }

    private static Coordinates getNextCoordinates(Direction direction, Coordinates coordinates, int factor) {
        if (direction == null) {
            throw new NullPointerException("Cannot give adjacent coordinates for null direction");
        }

        if (direction == Direction.INITIAL) {
            throw new IllegalArgumentException("Cannot give adjacent coordinates for INITIAL direction");
        }

        if (coordinates == null) {
            throw new NullPointerException("Cannot give adjacent coordinates for null coordinates");
        }

        int y = coordinates.getY() + (factor * direction.yAppend);
        int x = coordinates.getX() + (factor * direction.xAppend);

        return new Coordinates(y, x);
    }

    /**
     * Returns all moving Directions, i.e. every Direction except INITIAL
     */
    public static final Direction[] MOVING_DIRECTIONS = new Direction[] { UP, RIGHT, DOWN, LEFT };
}
