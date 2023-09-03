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
     * @return Maze parsed from the file found at the given filepath
     * @throws Exception caused by parsing the file, closing the file reader or the maze being in an invalid format
     */
    public static Maze parseMaze(String filepath) throws Exception {
        if (filepath == null) {
            throw new NullPointerException("Given filepath cannot be null");
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

    /**
     * Parses Tiles from a file using the given BufferedReader.
     * @param reader for the file
     * @return Nested List of the Tiles parsed from the file, where each sub-list represents a single line from the file
     */
    private static List<List<Tile>> parseTilesFromFile(BufferedReader reader) {
        return reader.lines().map(MazeParser::parseTilesFromLine).collect(Collectors.toList());
    }

    /**
     * Parses all of the Tiles from a single line parsed from the file.
     * @param line parsed from the file
     * @return all of the Tiles parsed from a single line from the file.
     */
    private static List<Tile> parseTilesFromLine(String line) {
        return line.chars()
                // chars() is actually an IntStream despite the name
                .mapToObj(i -> Tile.parseTile((char) i))
                .collect(Collectors.toList());
    }

    /**
     * Creates a Maze from the tiles parsed from the given file.
     * @param tilesPerLine Nested list of tiles parsed from the file, not null.
     * @return Maze created from the Tiles parsed from the file
     * @throws IllegalArgumentException if no tiles were able to be parsed from the file or if the individual maze rows
     * do not have the same width
     */
    private static Maze createMaze(List<List<Tile>> tilesPerLine) {
        if (tilesPerLine.isEmpty()) {
            throw new NullPointerException("Given file cannot be empty");
        }

        int width = tilesPerLine.get(0).size();

        if (!allRowsSameLength(tilesPerLine, width)) {
            throw new IllegalArgumentException("Given file must have rows of equal length");
        }

        return new Maze(createTileMatrix(tilesPerLine));
    }

    /**
     * Checks if all of the lines parsed from the line are the same length, i.e. given nested List has all sub-lists
     * being the same size
     * @param rows Nested List of Tiles, i.e. lines, parsed from the file
     * @param length which all of the individual rows should match
     * @return true if all rows are the same length, false if not
     */
    private static boolean allRowsSameLength(List<List<Tile>> rows, int length) {
        return rows.stream().noneMatch(row -> row.size() != length);
    }

    /**
     * Attempts to close the given BufferedReader if it is not null
     *
     * @param reader initialized for the file parsing
     * @throws IOException caused by reader.close()
     */
    private static void closeBufferedReader(BufferedReader reader) throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    /**
     * Creates a 2D array of Tiles based on the given Tiles per line.
     * Note that Tiles from tilesPerLine.get(i).get(j) are stored in Tile[i][j]
     * @param tilesPerLine Nested List of Tiles parsed from the file
     * @return 2D array of the Tiles parsed from the file
     */
    private static Tile[][] createTileMatrix(List<List<Tile>> tilesPerLine) {
        return tilesPerLine.stream()
                .map(line -> line.toArray(new Tile[0])) // Convert a nested list to Array
                .toArray(Tile[][]::new);
    }
}
