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

    /**
     * Starts the main program loop
     *
     * @throws Exception If closing the input reader fails
     */
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

    /**
     * Prints the error message and stack trace into the console
     * @param e
     */
    private static void printError(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    /**
     * The main program loop:
     * 1. Print instructions
     * 2. Wait for user to input a filename or 'x'
     * 3. If user inputted 'x' then exit the loop
     * 4. If the user inputted something else, then attempt to parse a file based on the filename user inputted
     * 5. If parsing was successful then attempt to solve the maze
     * 6. Start back at step 1, even if errors occured during any of the previous steps
     * @param reader
     */
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

    /**
     * Returns a boolean based on if the given string equals "x" (case insensitive)
     * @param input
     * @return
     */
    private static boolean isExitCommand(String input) {
        return input != null && Objects.equals("x", input.toLowerCase());
    }

}
