package com.maze.util;

import com.maze.domain.Maze;
import com.maze.domain.Tile;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class MazeParser {
    private MazeParser() {
        // Empty private constructor for static method class
    }

    /**
     * Creates a maze from parsing individual tiles from a file found from the given filepath.
     *
     * Should never be null, if parsing fails will throw an Exception.
     *
     * @param filepath not null
     * @return
     * @throws Exception caused by parsing the file, closing the file reader or the maze being in an invalid format
     */
    public static Maze parseMaze(String filepath) throws Exception {
        if (filepath == null) {
            throw new IllegalArgumentException("Given filepath cannot be null");
        }

        File file = new File(filepath);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            var tilesPerLine = parseTilesFromFile(reader);

            return createMaze(tilesPerLine);
        } finally {
            // Throws IOException if closing fails
            closeBufferedReader(reader);
        }
    }


    private static List<List<Tile>> parseTilesFromFile(BufferedReader reader) {
        return reader.lines().map(MazeParser::parseTilesFromLine).collect(Collectors.toList());
    }

    private static List<Tile> parseTilesFromLine(String line) {
        return line.chars()
                // chars() is actually an IntStream despite the name
                .mapToObj(i -> Tile.parseTile((char) i))
                .collect(Collectors.toList());
    }

    /**
     * Creates a maze from the tiles parsed from the given file.
     * @param tilesPerLine
     * @return
     * @throws IllegalArgumentException if no tiles were able to be parsed from the file or if the individual maze rows
     * do not have the same width
     */
    private static Maze createMaze(List<List<Tile>> tilesPerLine) {
        if (tilesPerLine.isEmpty()) {
            throw new IllegalArgumentException("Given file cannot be empty");
        }

        int width = tilesPerLine.get(0).size();

        if (!allRowsSameLength(tilesPerLine, width)) {
            throw new IllegalArgumentException("Given file must have rows of equal length");
        }

        return new Maze(createTileMatrix(tilesPerLine));
    }

    private static boolean allRowsSameLength(List<List<Tile>> rows, int length) {
        return rows.stream().noneMatch(row -> row.size() != length);
    }

    /**
     * Attemps to close the given BufferedReader if it is not null
     * s
     * @param reader
     * @throws IOException caused by reader.close()
     */
    private static void closeBufferedReader(BufferedReader reader) throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    /**
     * Creates a 2D array of Tiles based on the given Tiles per line.
     * Tile from tilesPerLine.get(i).get(j) is stored in Tile[i][j]
     * @param tilesPerLine
     * @return
     */
    private static Tile[][] createTileMatrix(List<List<Tile>> tilesPerLine) {
        return tilesPerLine.stream()
                .map(line -> line.toArray(new Tile[0])) // Convert a nested list to Array
                .toArray(Tile[][]::new);
    }
}
