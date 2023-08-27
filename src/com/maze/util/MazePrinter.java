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

    /**
     * Prints the results of the maze solving.
     * If the maze was solvable then prints an ASCII graphic of the solution on the console.
     * If the maze was not solvable then prints a message stating such.
     *
     * @param maze not null
     */
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

        System.out.println();
    }

    private static void printMaze(Maze maze) {
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                printCharAtCoordinates(maze, new Coordinates(y, x));
            }

            System.out.println();
        }
    }

    /**
     * Prints an individual char for the given coordinates on the solution print.
     * If the given coordinates were a part of the solution the prints the char in a Cyan coluor.
     * If the given coordinates were NOT a part of the solution then prints the color in the console's default colour.
     * @param maze
     * @param coordinates
     */
    private static void printCharAtCoordinates(Maze maze, Coordinates coordinates) {
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
        String charString = coordinatesPartOfSolution ? colourCharInCyan(printedChar) : "" + printedChar;
        System.out.print(charString);
    }

    private static String colourCharInCyan(char printedChar) {
        return ANSI_CYAN + printedChar + ANSI_RESET; // need to reset color back to default after the char
    }
}
