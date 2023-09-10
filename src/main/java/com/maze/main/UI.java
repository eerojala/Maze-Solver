package com.maze.main;

import com.maze.domain.Maze;
import com.maze.domain.SolutionStatus;
import com.maze.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class UI {
    private UI() {
        // Empty private method for static method class
    }

    /**
     * Starts the main program loop. Will cause an IOException if closing the input reader fails.
     *
     */
    public static void start() {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        loopProgramUntilExit(reader);

        try {
            reader.close();
        } catch (IOException e) {
            Printer.println("Error while closing input reader: " + e.getMessage());
        }
    }

    /**
     * The main program loop:
     * 1. Print instructions
     * 2. Wait for user to input a filename or 'x' (case insensitive)
     * 3. If user inputted 'x' then exit the loop
     * 4. If the user inputted something else, then attempt to parse a file based on the filename user inputted
     * 5. If parsing was successful then attempt to solve the maze
     * 6. Print the solving results into console
     * 7. If the solving was successful then also write the solution into a text file
     * 8. Start back at step 1, even if errors occurred during any of the previous steps
     *
     * Will terminate execution if there were any IOExceptions while reading user input or closing the
     * file reader/-writer.
     *
     * @param reader BufferedReader used for reading user input.
     */
    private static void loopProgramUntilExit(BufferedReader reader) {
        while (true) {
            printInstructions();
            String input = readInput(reader);
            /*
             * Input is null if there was an IOException while reading user input, in which case we want to close
             * application.
             */
            if (input == null || isExitCommand(input)) {
                break;
            }

            Maze maze;

            try {
                maze = MazeParser.parseMaze(input);
            } catch (IOException e) {
                /*
                 * IOException is thrown if was unable to close file reader.
                 * If this happens then print error and close this application.
                 * Other errors that might happen during parsing are caught within the parseMaze method and do not
                 * require application terminations.
                 * (parseMaze can also throw NullPointerException if given a null parameter but we check it beforehand
                 * in this method so should never happen)
                 */
                Printer.println("\n" + e.getMessage());
                break;
            }

            if (maze != null) {
                MazeSolver.attemptToSolveMaze(maze);
                boolean fileWriteUnsuccessful = !handleSolutionResult(maze);

                if (fileWriteUnsuccessful) {
                    break;
                }
            }

            Printer.println(); // empty line print to make UI more clear
        }
    }

    private static void printInstructions() {
        Printer.println("Instructions:");
        Printer.println("Enter a file name (include file ending) to attempt to parse and solve a maze from the file");
        Printer.println("Enter x to exit the program");
        Printer.print("Filename: ");
    }

    /**
     * Attempts to read user input.
     * @param reader BufferedReader from which the user input is read from.
     * @return String read from user input, null if there was an IOException.
     */
    private static String readInput(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            Printer.println("Error while reading user input: " + e.getMessage());

            return null;
        }
    }

    /**
     * Returns a boolean based on if the given string equals "x" (case insensitive)
     * @param input User input
     * @return True if input equals to 'x' (case insensitive), false otherwise.
     */
    private static boolean isExitCommand(String input) {
        return input != null && Objects.equals("x", input.toLowerCase());
    }

    /**
     * Performs further actions for solution printing/writing depending on the solution result.
     *
     * If the solving was successful, print the solution and write it to a text file.
     *
     * If the solving was unsuccessful, print a message stating such.
     *
     * If the solving resulted in an error does nothing.
     *
     * @param maze which was attempted to be solved.
     * @return false if solving was successful but was unable to write the solution to a file, true otherwise
     */
    private static boolean handleSolutionResult(Maze maze) {
        var resultStatus = maze.getSolutionStatus();

        if (resultStatus != SolutionStatus.ERROR) {
            String solutionPrintAscii = SolutionWriter.createSolutionAscii(maze, true);
            printResult(maze, solutionPrintAscii);

            if (resultStatus == SolutionStatus.SUCCESS) {
                String solutionFileAscii = SolutionWriter.createSolutionAscii(maze, false);

                // Return boolean based on if file write was successful or not
                return writeSolutionIntoFile(solutionFileAscii);
            }
        }

        return true;
    }

    /**
     * Prints the results of the maze solving.
     * 
     * If maze.getSolutionStatus() == SUCCESS then given solution graphic + information about amount of steps required
     * for the solution into the console.
     *
     * If maze.getSolutionStatus() == FAILURE and(or solution ascii is null then prints message stating that the maze
     * was not solvable within the maximum limit.
     *
     * @param maze not null and maze.getSolutionStatus() not ERROR
     * @param solutionGraphic ASCII graphic to be printed if maze was solved.
     */
    private static void printResult(Maze maze, String solutionGraphic)  {
        if (maze == null) {
            throw new NullPointerException("Maze must not be null in order for the solution to be printed");
        }

        var solutionStatus = maze.getSolutionStatus();

        if (solutionStatus == SolutionStatus.ERROR) {
            throw new IllegalArgumentException("Given maze must not have ERROR as the solution status");
        }

        Printer.println();

        int stepLimit = maze.getStepLimit();

        if (maze.getSolutionStatus() == SolutionStatus.SUCCESS && solutionGraphic != null) {
            Printer.println("Maze was solvable within " + stepLimit + " steps");
            Printer.println("Solution with " + maze.getCurrentStepCount() + " steps:");
            Printer.println(solutionGraphic);
        } else {
            Printer.println("Maze was not solvable within " + stepLimit + " steps");
        }
    }

    /**
     * Writes the given solution ASCII graphic (if not null) into a new text file.
     * If the file writing operation was successful then also prints the filename into the console.
     * If the file writing was unsuccessful prints a message stating such into the console.ö
     *
     * @param solutionGraphic Solution ASCII graphic to be written into the file.
     * @return true if file writing was successful, false otherwise
     */
    private static boolean writeSolutionIntoFile(String solutionGraphic) {
        if (solutionGraphic != null) {
            try {
                String filename = SolutionWriter.writeSolutionGraphicIntoTextFile(solutionGraphic);

                if (filename != null) {
                    Printer.println("\nSolution written into " + filename);

                    return true;
                }
            } catch (IOException e) {
                Printer.println("\nError while closing file writer: "  + e.getMessage());
            }
        }

        return false;
    }
}
