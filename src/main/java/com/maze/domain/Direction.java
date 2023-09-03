package com.maze.domain;

public enum Direction {
    UP('\u2191', 'U',-1, 0),
    RIGHT('\u2192', 'R', 0, 1),
    DOWN('\u2193', 'D', 1, 0),
    LEFT('\u2190', 'L', 0, -1);

    private final char arrowChar;
    private final char letterChar;
    private final int yAppend;
    private final int xAppend;

    Direction(char arrowChar, char letterChar, int yAppend, int xAppend) {
        this.arrowChar = arrowChar;
        this.letterChar = letterChar;
        this.yAppend = yAppend;
        this.xAppend = xAppend;
    }

    /**
     * Returns an arrow character representation of the Direction.
     * UP = '↑'
     * RIGHT = '→'
     * DOWN = '↓'
     * LEFT = '←'
     * @return an arrow character representation of the Direction.
     */
    public char getArrowChar() {
        return arrowChar;
    }

    /**
     * Returns a single letter representation of the Direction.
     * UP = 'U'
     * RIGHT = 'R'
     * DOWN = 'D'
     * LEFT 'L'
     * @return a single letter representation of the Direction.
     */
    public char getLetterChar() {
        return letterChar;
    }

    /**
     * Returns coordinates next to the given coordinates from the given direction, e.g. if direction is UP, then returns
     * coordinates which are above.
     * @param direction not null
     * @param coordinates not null
     * @return coordinates next to the given coordinates from the given direction
     */
    public static Coordinates getNextCoordinates(Direction direction, Coordinates coordinates) {
        return getNextCoordinates(direction, coordinates, 1);
    }

    /**
     * Returns coordinates previous from the given coordinates and the given direction, e.g. if direction is UP, then
     * returns coordinates which are below
     *
     * @param direction not null
     * @param coordinates not null
     * @return coordinates previous from the given coordinates from the given direction
     */
    public static Coordinates getPreviousCoordinates(Direction direction, Coordinates coordinates) {
        return getNextCoordinates(direction, coordinates, -1);
    }

    private static Coordinates getNextCoordinates(Direction direction, Coordinates coordinates, int factor) {
        if (direction == null) {
            throw new NullPointerException("Cannot give adjacent coordinates for null direction");
        }

        if (coordinates == null) {
            throw new NullPointerException("Cannot give adjacent coordinates for null coordinates");
        }

        int y = coordinates.getY() + (factor * direction.yAppend);
        int x = coordinates.getX() + (factor * direction.xAppend);

        return new Coordinates(y, x);
    }
}
