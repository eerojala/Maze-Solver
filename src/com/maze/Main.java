package com.maze;

import com.maze.domain.Coordinates;
import com.maze.domain.Maze;
import com.maze.util.MazeParser;
import com.maze.util.MazePrinter;
import com.maze.util.MazeSolver;

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
                try {
                    // String filePath = reader.readLine();
                    Maze maze = MazeParser.parseMaze("maze1.txt");
                    MazeSolver.attemptToSolveMaze(maze);
                    MazePrinter.printSolution(maze);
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
}
