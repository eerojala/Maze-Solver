package com.maze;

import com.maze.domain.Maze;
import com.maze.util.MazeParser;
import com.maze.util.MazePrinter;
import com.maze.util.MazeSolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * TODO:
 * Uncomment UI code and make it loop properly
 */

public class Main {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));

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
        // IOException from close is thrown outside of main
        reader.close();
    }
}
