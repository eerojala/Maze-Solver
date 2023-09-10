package com.maze.util;

import com.maze.domain.Coordinates;
import com.maze.domain.Direction;
import com.maze.domain.Maze;
import com.maze.domain.SolutionStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SolutionWriter {
    private SolutionWriter() {
        // Empty private constructor for
    }

    /**
     * Creates a ASCII graphic for the maze solution where the solution path is overlayed to the maze parsed from the
     * text file.

     * @param maze Maze from which the solution graphic will be drawn from.
     * @param forPrint Boolean for determining if the ASCII is created for console (true) or file (false) output.
     *                 If true then will represent the solution path with UTF-8 arrows.
     *                 If false then will represent the solution path with single characters representing directions
     *                 (e.g. 'U' for up, 'R' for right, etc..)
     * @return ASCII graphic String of the solution. If given Maze is null or unsolved then null.
     */
    public static String createSolutionAscii(Maze maze, boolean forPrint) {
        if (maze == null || maze.getSolutionStatus() != SolutionStatus.SUCCESS) {
            return null;
        }

        var stringBuilder = new StringBuilder();

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                stringBuilder.append(getCharForCoordinates(maze, new Coordinates(y, x), forPrint));
            }

            if (y < maze.getHeight() - 1) {
                stringBuilder.append("\n"); // No newline for final line
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Returns an individual char for the given Coordinates to be used in the solution ASCII graphic.
     * If the given Coordinates are a part of the solution will return a char representing the Direction towards the
     * exit.
     * If the given Coordinates are not part of the solution will then return a char representing the Tile found in the
     * given Maze at the given Coordinates.
     *
     * @param maze Maze from which the solution graphic will be drawn from.
     * @param coordinates Coordinates which the returned char will represent.
     * @param forPrint Boolean for determining if the ASCII graphic is created for console (true) or file (false) output.
     *                 If true then will represent Coordinates with an UTF-8 arrow if they are part of the solution.
     *                 If false then will represent Coordinates with a single character representing a Direction if they
     *                 are a part of the solution (e.g. 'U' for up, 'R' for right, etc..).
     * @return char representing the given Coordinates in the solution ASCII graphic.
     */
    private static char getCharForCoordinates(Maze maze, Coordinates coordinates, boolean forPrint) {
        Direction direction = maze.getDirectionFromSolutionPath(coordinates);

        return direction != null
                ? getDirectionChar(direction, forPrint)
                : maze.getTileForCoordinates(coordinates).getChar();
    }

    /**
     * Returns an individual char for the given Direction towards the exit in the solution path.
     * @param direction Direction towards the exit in the solution path.
     * @param forPrint Boolean for determining if the char will be used for console (true) or file (false) output
     * @return If forPrint == true returns an UTF-8 arrow character representing the Direction.
     *         If forPrint == false then returns a letter character representing the Direction. (e.g. 'U' for up,
     *         'R' for right, etc..)
     */
    private static char getDirectionChar(Direction direction, boolean forPrint) {
        return forPrint ? direction.getLetterChar() : direction.getArrowChar();
    }

    /**
     * Writes the given solution graphic into a text file.
     * The file name for the solution file will be "solution-{currentTime}"
     * Returns the file name if write operation was successful.
     *
     * @param solutionGraphic ASCII graphic to be written to the file.
     * @return Generated filename for the written file if file writing was successful, null otherwise.
     * @throws IOException caused by IOUtil.close
     */
    public static String writeSolutionGraphicIntoTextFile(String solutionGraphic) throws IOException {
        String filename = "solution-" + getCurrentTimeString() + ".txt";
        PrintWriter writer =  new PrintWriter(filename, StandardCharsets.UTF_8);

        try {
            writer = new PrintWriter(filename, StandardCharsets.UTF_8);
            writer.write(solutionGraphic);

            return filename;
        } catch (IOException e) {
            Printer.println("Was unable write the solution to file " + filename);

            return null;
        } finally {
            IOUtil.close(writer);
        }
    }

    /**
     * Returns a string representation of LocalDateTime.now() with colons and dots replaced by dashes.
     * @return String representation of LocalDateTime.now().
     */
    private static String getCurrentTimeString() {
        String regex = "[:.]"; // ':' or '.'
        return LocalDateTime.now().toString().replaceAll(regex, "-");
    }
}
