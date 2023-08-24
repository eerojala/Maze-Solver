package com.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                }
            }
        } finally {
            // IOException from close is thrown outside of main
            reader.close();
        }
    }

    private static void solveMaze(Tile[][] maze) {
        var solver = new MazeSolver();
        var solution = solver.solveMaze(maze);

        if (solution == null) {
            System.out.println("Maze not solvable");
        } else {
            printSolution(solution);
        }
    }

    private static void printSolution(char[][] solution) {
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
