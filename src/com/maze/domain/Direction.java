package com.maze.domain;

public enum Direction {
    UP('↑'),
    RIGHT('→'),
    DOWN('↓'),
    LEFT('←');

    private final char ch;

    Direction(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return ch;
    }
}
