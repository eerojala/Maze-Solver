package com.maze.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Tile {
    // In order of most common to rarest
    BLOCK('#'),
    SPACE(' '),
    EXIT('E'),
    START('^');

    private final char ch;

    Tile(char ch) {
        this.ch = ch;
    }

    /**
     * Returns a char representation of the Tile.
     * BLOCK = '#'
     * SPACE = ' '
     * EXIT = 'E'
     * START = '^'
     * @return char representation of the Tile
     */
    public char getChar() {
        return ch;
    }

    /**
     * Parses a tile type from given char.
     * Possible parsable characters are '#', ' ', 'E' and '^'
     *
     * @param ch char to be parsed
     * @return TileType parsed from char
     * @throws NoSuchElementException If the given char is not a parsable character
     */
    public static Tile parseTile(char ch) {
        return Arrays.stream(Tile.values())
                .filter(type -> type.getChar() == ch)
                .findAny()
                .orElseThrow(() -> createNoSuchElementException(ch));
    }

    private static NoSuchElementException createNoSuchElementException(char ch) {
        String firstPart = "Could not parse character ";
        String latterPart = " into a Tile. Please make sure the file contains only characters '#', ' ', 'E' or '^'";

        return new NoSuchElementException(firstPart + ch + latterPart);
    }
}
