package com.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                // System.out.print("Enter file name: ");

                try {
                    // String filePath = reader.readLine();
                    var maze = MazeParser.parseMaze("maze1.txt");
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
            printSolution(solution, limit);
            return true;
        }

        return false;
    }

    private static void printSolution(char[][] solution, int limit) {
        System.out.println("Maze solvable within " + limit + " steps: ");

        for (int y = 0; y < solution.length; y++) {
            for (int x = 0; x < solution[y].length; x++) {
                System.out.print(solution[y][x]);
            }

            System.out.println();
        }
    }

    private static void printMaze(Tile[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j].getSymbol());
            }

            System.out.println();
        }
    }
}
