package com.maze.domain;

public enum Direction {
    UP('↑', -1, 0),
    RIGHT('→', 0, 1),
    DOWN('↓', 1, 0),
    LEFT('←', 0, -1);

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

    public Coordinates getNextCoordinates(Coordinates coordinates) {
        int y = coordinates.getY() + yAppend;
        int x = coordinates.getX() + xAppend;

        return new Coordinates(y, x);
    }
}
