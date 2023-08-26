package com.maze;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class MazeParser {
    public static Maze parseMaze(String filepath) throws IOException {
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
                // chars() is actually an IntStream so need to convert the integers to characters
                .mapToObj(i -> Character.valueOf((char) i))
                .map(Tile::parseTile)
                .collect(Collectors.toList());
    }

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

    private static void closeBufferedReader(BufferedReader reader) throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    private static Tile[][] createTileMatrix(List<List<Tile>> tilesPerLine) {
        return tilesPerLine.stream()
                .map(line -> line.toArray(new Tile[line.size()])) // Convert a nested list to Array
                .toArray(Tile[][]::new);
    }
}
