package com.maze.main;

import com.maze.domain.Maze;
import com.maze.util.*;

import java.io.BufferedReader;
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

        try {
            loopProgramUntilExit(reader);
            reader.close();
        } catch (Exception e) {
            // Error attempting to close reader
            e.printStackTrace();
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
     * @param reader BufferedReader used for reading user input.
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
                boolean solvable = MazeSolver.isMazeSolvable(maze);

                if (solvable) {
                    String solutionPrintAscii = SolutionWriter.createSolutionAscii(maze, true);
                    printResult(maze, solutionPrintAscii);
                    String solutionFileAscii = SolutionWriter.createSolutionAscii(maze, false);
                    writeSolutionIntoFile(solutionFileAscii);
                } else {
                    printResult(maze, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
     * @param input User input
     * @return True if input equals to 'x' (case insensitive), false otherwise.
     */
    private static boolean isExitCommand(String input) {
        return input != null && Objects.equals("x", input.toLowerCase());
    }

    /**
     * Prints the results of the maze solving.
     * 
     * If maze was solvable then given solution graphic + information about amount of steps required for the solution
     * into the console.
     *
     * If maze was unsolved and/or solution ascii is null then prints message stating that the maze was not solvable
     * within the maximum limit.
     *
     * @param maze not null
     * @param solutionGraphic ASCII graphic to be printed if maze was solved.
     */
    private static void printResult(Maze maze, String solutionGraphic)  {
        if (maze == null) {
            throw new NullPointerException("Maze must not be null in order for the solution to be printed");
        }

        int stepLimit = maze.getStepLimit();

        if (maze.isSolved() && solutionGraphic != null) {
            System.out.println("Maze was solvable within " + stepLimit + " steps");
            System.out.println("Solution with " + maze.getCurrentStepCount() + " steps:");
            System.out.println(solutionGraphic);
        } else {
            System.out.println("Maze was not solvable within " + stepLimit + " steps");
        }

    }

    /**
     * Writes the given solution ASCII graphic (if not null) into a new text file.
     * If the file writing operation was successful then also prints the filename into the console.
     *
     * @param solutionGraphic Solution ASCII graphic to be written into the file.
     * @throws Exception caused by trying to write the solution ASCII graphic into a new text file.
     */
    private static void writeSolutionIntoFile(String solutionGraphic) throws Exception {
        if (solutionGraphic != null) {
            String filename = SolutionWriter.writeSolutionGraphicIntoTextFile(solutionGraphic);

            if (filename != null) {
                System.out.println("Solution written into " + filename + "\n");
            }
        }
    }

}
