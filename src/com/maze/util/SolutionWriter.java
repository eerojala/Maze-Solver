package com.maze.util;

import com.maze.domain.Coordinates;
import com.maze.domain.Direction;
import com.maze.domain.Maze;
import com.maze.domain.Tile;

import java.io.PrintWriter;
import java.time.LocalDateTime;

public class SolutionWriter {
    private SolutionWriter() {
        // Empty private constructor for
    }

    /**
     * Creates a ASCII graphic for the maze solution where the solution path is overlayed to the maze parsed from the
     * text file.
     *
     * Returns null if given maze is null or unsolved.
     *
     * @param maze
     * @return
     */
    public static String createSolutionAscii(Maze maze) {
        if (maze == null || !maze.isSolved()) {
            return null;
        }

        var stringBuilder = new StringBuilder();

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                stringBuilder.append(getCharForCoordinates(maze, new Coordinates(y, x)));
            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Returns a char for the given coordinates based on if it is part of the solution or not.
     *
     * If the coordinates are a part of the solution path, then return a char from the solution matrix.
     *      If the coordinates are at an START or EXIT Tile, then returns '^' or 'E' respectively.
     *      Otherwise returns an arrow char representing the direction toward an EXIT from the START
     *
     * If the coordinates ARE not a part of the solution path, then return a char based on which Tile is located
     * at the coordinates
     *
     * @param maze
     * @param coordinates
     */
    private static char getCharForCoordinates(Maze maze, Coordinates coordinates) {
        Direction direction = maze.getDirectionFromSolutionPath(coordinates);
        Tile tile = maze.getTileForCoordinates(coordinates);

        return direction != null && tile == Tile.SPACE // Do not mark entrance and exit with direction
                ? direction.getChar()
                : maze.getTileForCoordinates(coordinates).getChar();
    }

    /**
     * Writes the given solution graphic into a text file.
     * The file name for the solution file will be "solution-{currentTime}"
     * Returns the file name if write operation was successful.
     *
     * @param solutionGraphic
     * @return
     * @throws Exception caused by writing into the file
     */
    public static String writeSolutionGraphicIntoTextFile(String solutionGraphic) throws Exception {
        String filename = "solution-" + getCurrentTimeString() + ".txt";
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(filename, "UTF-8");
            writer.write(solutionGraphic);

            return filename;
        } finally {
            closeWriter(writer);
        }
    }

    /**
     * Returns a string representaion of LocalDateTime.now() with colons and dots replaced by dashes.
     * @return
     */
    private static String getCurrentTimeString() {
        String regex = ":|\\."; // ':' or '.'
        return LocalDateTime.now().toString().replaceAll(regex, "-");
    }

    /**
     * Closes given PrintWriter if it is not null
     * @param writer
     */
    private static void closeWriter(PrintWriter writer) {
        if (writer != null) {
            writer.close();
        }
    }
}
