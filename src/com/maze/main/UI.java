package com.maze.main;

import com.maze.domain.Maze;
import com.maze.util.MazeParser;
import com.maze.util.MazePrinter;
import com.maze.util.MazeSolver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class UI {
    private UI() {
        // Empty private method for static method class
    }

    public static void start() {
        var reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            loopProgramUntilExit(reader);
            reader.close();
        } catch (Exception e) {
            // Error attempting to close reader
            printError(e);
        }
    }

    private static void printError(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    private static void loopProgramUntilExit(BufferedReader reader) {
        while (true) {
            try {
                printInstructions();
                String input = reader.readLine();

                if (isExitCommand(input)) {
                    break;
                }

                Maze maze = MazeParser.parseMaze(input);
                MazeSolver.attemptToSolveMaze(maze);
                MazePrinter.printSolution(maze);
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    private static void printInstructions() {
        System.out.println("Instructions:");
        System.out.println("Enter a file name (include file ending) to attempt to parse and solve a maze from the file");
        System.out.println("Enter x to exit the program");
        System.out.print("Filename: ");
    }

    private static boolean isExitCommand(String input) {
        return Objects.equals("x", input);
    }

}
