package com.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/*
 * TODO:
 * Uncomment UI code and make it loop properly
 * Make new class MazePrinter
 */

public class Main {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                // System.out.print("Enter file name: ");

                try {
                    // String filePath = reader.readLine();
                    Maze maze = MazeParser.parseMaze("maze2.txt");
                    solveMaze(maze);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            // IOException from close is thrown outside of main
            reader.close();
        }
    }

    private static void solveMaze(Maze maze) {
        var limits = new int[] { 20, 150, 200 };
        var foundSolution = Arrays.stream(limits)
                .filter(limit -> solvableWithinLimit(maze, limit))
                .findFirst();

        if (foundSolution.isEmpty()) {
            System.out.println("Maze not solvable within 200 steps");
        }
    }

    private static boolean solvableWithinLimit(Maze maze, int limit) {
        MazeSolver.attemptToSolveMaze(maze, limit);

        if (maze.isSolved()) {
            printSolution(maze, limit);
            return true;
        }

        return false;
    }

    private static void printSolution(Maze maze, int limit) {
        System.out.println("Maze solvable within " + limit + " steps: ");

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                System.out.print(getCharForPrint(maze, new Coordinates(y, x)));
            }

            System.out.println();
        }
    }

    private static char getCharForPrint(Maze maze, Coordinates coordinates) {
        char charFromSolution = maze.getSolutionCharFromCoordinates(coordinates);
        char charFromMaze = maze.getTileForCoordinates(coordinates).getChar();

        /**
         * char arrays are initialized with default values (=null char) and in the solution arrays new values are only
         * added for the tiles which are part of the solution path.
         * So to print the entire maze with solution we need to use the original maze to get the chars from tiles which
         * are not part of the solution.
         */
        return charFromSolution != '\u0000' // null char
                ? charFromSolution
                : charFromMaze;

    }
}
