package com.maze;

public enum Direction {
    UP('↑'),
    RIGHT('→'),
    DOWN('↓'),
    LEFT('←'),
    INITIAL('?'); // Only used for the initial starting position, not printed

    private final char symbol;

    private Direction(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static final Direction[] MOVING_DIRECTIONS = new Direction[] { UP, RIGHT, DOWN, LEFT };
}
