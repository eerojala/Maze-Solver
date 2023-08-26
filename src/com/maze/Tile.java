package com.maze;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Tile {
    // In order of most common to rarest
    BLOCK('#'),
    SPACE(' '),
    EXIT('E'),
    START('^');

    private final char symbol;

    private Tile(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    /**
     * Parses a tile type from given char.
     * Possible parsable characters are '#', ' ', 'E' and '^'.
     * If the given char is not any of these characters, then method will throw NoSuchElementException
     *
     * @param ch char to be parsed
     * @return TileType parsed from symbol
     */
    public static Tile parseTileType(char ch) {
        return Arrays.stream(Tile.values())
                .filter(type -> type.getSymbol() == ch)
                .findAny()
                .orElseThrow(() -> createNoSuchElementException(ch));
    }

    private static NoSuchElementException createNoSuchElementException(char symbol) {
        String firstPart = "Could not parse character ";
        String latterPart = " into a TileType. Please make sure the file contains only characters '#', ' ', 'E' or '^'";
;
        return new NoSuchElementException(firstPart + symbol + latterPart);
    }
}
