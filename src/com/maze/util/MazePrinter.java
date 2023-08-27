package com.maze.util;

import com.maze.domain.Coordinates;
import com.maze.domain.Maze;

public class MazePrinter {
    /**
     * For printing cyan coloured text
     */
    private static final String ANSI_CYAN = "\u001B[36m";

    /**
     * For resetting the text print color to default
     */
    private static final String ANSI_RESET = "\u001B[0m";

    private MazePrinter() {
        // Empty private constructor for static method class
    }

    public static void printSolution(Maze maze) {
        if (maze == null) {
            throw new IllegalArgumentException("Printable maze cannot be null");
        }

        if (maze.isSolved()) {
            System.out.println("Maze solvable within " + maze.getStepLimit() + " steps");
            System.out.println("Solution with " + maze.getSolutionStepCount() + " steps:");
            printMaze(maze);
        } else {
            System.out.println("Maze not solvable within " + maze.getStepLimit() + " steps");
        }

        System.out.println("");
    }

    private static void printMaze(Maze maze) {
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                printChar(maze, new Coordinates(y, x));
            }

            System.out.println();
        }
    }

    private static void printChar(Maze maze, Coordinates coordinates) {
        char charFromSolution = maze.getSolutionCharFromCoordinates(coordinates);
        char charFromMaze = maze.getTileForCoordinates(coordinates).getChar();

        /*
         * char arrays are initialized with default values (=null char) and in the solution array new values are only
         * added for the tiles which are part of the solution path.
         * So to print the entire maze with solution we need to use the original maze to get the chars from tiles which
         * are not part of the solution.
         */
        boolean coordinatesPartOfSolution = charFromSolution != '\u0000'; // null char
        char printedChar = coordinatesPartOfSolution ? charFromSolution : charFromMaze;
        String colorPrefix = coordinatesPartOfSolution ? ANSI_CYAN : "";

        System.out.print(colorPrefix + printedChar + ANSI_RESET);
    }
}
