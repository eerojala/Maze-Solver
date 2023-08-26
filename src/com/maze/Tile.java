package com.maze;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Tile {
    // In order of most common to rarest
    BLOCK('#'),
    SPACE(' '),
    EXIT('E'),
    START('^');

    private final char ch;

    private Tile(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return ch;
    }

    /**
     * Parses a tile type from given char.
     * Possible parsable characters are '#', ' ', 'E' and '^'.
     * If the given char is not any of these characters, then method will throw NoSuchElementException
     *
     * @param ch char to be parsed
     * @return TileType parsed from char
     */
    public static Tile parseTile(char ch) {
        return Arrays.stream(Tile.values())
                .filter(type -> type.getChar() == ch)
                .findAny()
                .orElseThrow(() -> createNoSuchElementException(ch));
    }

    private static NoSuchElementException createNoSuchElementException(char ch) {
        String firstPart = "Could not parse character ";
        String latterPart = " into a TileType. Please make sure the file contains only characters '#', ' ', 'E' or '^'";
;
        return new NoSuchElementException(firstPart + ch + latterPart);
    }
}
