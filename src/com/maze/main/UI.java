package com.maze.main;

import com.maze.domain.Maze;
import com.maze.util.MazeParser;
import com.maze.util.MazeSolver;
import com.maze.util.SolutionWriter;

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
            e.printStackTrace();
        }
    }

    /**
     * The main program loop:
     * 1. Print instructions
     * 2. Wait for user to input a filename or 'x'
     * 3. If user inputted 'x' then exit the loop
     * 4. If the user inputted something else, then attempt to parse a file based on the filename user inputted
     * 5. If parsing was successful then attempt to solve the maze
     * 6. Print the solving results into console
     * 7. If the solving was successful then also write the solution into a text file
     * 8. Start back at step 1, even if errors occured during any of the previous steps
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
                MazeSolver.solveMaze(maze);
                String solutionPrintAscii = SolutionWriter.createSolutionAscii(maze, true);
                printSolution(maze, solutionPrintAscii);

                if (solutionPrintAscii != null) {
                    String solutionFileAscii = SolutionWriter.createSolutionAscii(maze, false);
                    writeSolutionIntoFile(solutionFileAscii);
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
     * @param input
     * @return
     */
    private static boolean isExitCommand(String input) {
        return input != null && Objects.equals("x", input.toLowerCase());
    }

    /**
     * Prints given solution graphic + information about amount of steps required for the solution into the console.
     *
     * If maze is unsolved and/or soltion graphic is null then prints message stating that the maze was not solvable.
     *
     * @param maze not null
     * @param solutionGraphic
     */
    private static void printSolution(Maze maze, String solutionGraphic)  {
        if (maze == null) {
            throw new NullPointerException("Maze must not be null in order for the solution to be printed");
        }

        int stepLimit = maze.getCurrentStepLimit();

        if (maze.isSolved() && solutionGraphic != null) {
            System.out.println("Maze was solvable within " + stepLimit + " steps");
            System.out.println("Solution with " + maze.getCurrentStepCount() + " steps:");
            System.out.println(solutionGraphic);
        } else {
            System.out.println("Maze was not solvable within " + stepLimit);
        }

    }

    /**
     * Writes the given solution graphic (if not null) into a text file.
     * If the file writing operation was successful then also prints the filename into the console.
     *
     * @param solutionGraphic
     * @throws Exception
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
