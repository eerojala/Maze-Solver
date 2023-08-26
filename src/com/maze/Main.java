package com.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/*
 * TODO:
 * Rename symbol -> char
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
                    var maze = MazeParser.parseMaze("maze2.txt");
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

    private static void solveMaze(Tile[][] maze) {
        var limits = new int[] { 20, 50, 200 };
        var foundSolution = Arrays.stream(limits)
                .filter(limit -> solvableWithinLimit(maze, limit))
                .findFirst();

        if (foundSolution.isEmpty()) {
            System.out.println("Maze not solvable within 200 steps");
        }
    }

    private static boolean solvableWithinLimit(Tile[][] maze, int limit) {
        var solver = new MazeSolver();
        var solution = solver.solveMaze(maze, limit);

        if (solution != null) {
            printSolution(maze, solution, limit);
            return true;
        }

        return false;
    }

    private static void printSolution(Tile[][] maze, char[][] solution, int limit) {
        System.out.println("Maze solvable within " + limit + " steps: ");

        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                System.out.print(getSymbolForPrint(maze, solution, y, x));
            }

            System.out.println();
        }
    }

    private static char getSymbolForPrint(Tile[][] maze, char[][] solution, int y, int x) {
        char symbolFromSolution = solution[y][x];
        char symbolFromMaze = maze[y][x].getSymbol();

        /**
         * char arrays are initialized with default values (=null char) and in the solution arrays new values are only
         * added for the tiles which are part of the solution path.
         * So to print the entire maze with solution we need to use the original maze to get the chars from tiles which
         * are not part of the solution.
         */
        return symbolFromSolution != '\u0000' // null char
                ? symbolFromSolution
                : symbolFromMaze;

    }
}
