package com.maze;

public enum Direction {
    UP('↑'),
    RIGHT('→'),
    DOWN('↓'),
    LEFT('←'),
    INITIAL('?'); // Only used for the initial starting position, not printed

    private final char ch;

    private Direction(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return ch;
    }

    public static final Direction[] MOVING_DIRECTIONS = new Direction[] { UP, RIGHT, DOWN, LEFT };
}
