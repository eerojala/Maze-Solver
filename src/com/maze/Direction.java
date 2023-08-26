package com.maze;

public enum Direction {
    UP('↑'),
    RIGHT('→'),
    DOWN('↓'),
    LEFT('←');

    private final char ch;

    private Direction(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return ch;
    }
}
