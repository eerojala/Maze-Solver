package com.maze.util;

import com.maze.domain.Coordinates;
import com.maze.domain.Direction;
import com.maze.domain.Maze;
import com.maze.domain.Tile;

import java.util.Arrays;

public class MazeSolver {
    private static final int[] LIMITS = { 20, 150, 200 };

    private MazeSolver() {
        // Private empty constructor for static method class
    }

    /**
     * Attemps to solve the maze within 20/150/200 steps.
     * If an solution is found then sets the maze as solved and marks the solution coordinates and stops looping through
     * the step limits.
     *
     * If no solution is found within none of the steps then method execution stops after the final limit has been
     * tested and the maze.solved -flag remains as false.
     *
     * @param maze
     */
    public static void attemptToSolveMaze(Maze maze) {
        if (maze == null) {
            throw new IllegalArgumentException("Solvable maze cannot be null");
        }

        for (int limit : LIMITS) {
            maze.resetProgress(limit);
            traverseMaze(maze);

            if (maze.isSolved()) {
                break;
            }
        }
    }

    /**
     * Method for traversing through the maze in an attempt to find an exit.
     * If method returns a non-null Direction, then an exit was found
     * The method is designed to be used recursively.
     *
     * See the below methods for more details.
     *
     * @param maze
     * @return
     */
    private static Direction traverseMaze(Maze maze) {
        if (!isCurrentPositionTraversable(maze)) {
            return null;
        }

        maze.markCurrentCoordinateTryStatus(true);
        Direction nextSuccessfulDirection = getSuccessfulDirection(maze);

        if (nextSuccessfulDirection != null) {
            updateSolution(maze, nextSuccessfulDirection);

            return maze.getCurrentDirection();
        } else {
            /*
             * If it was not possible to reach an exit within the step limit by trying to traverse as far as possible
             * through all directions from this tile then mark this tile as untried before returning.
             *
             * This is done so that this same tile can be tried later through a different route which might be faster
             * and thus maybe able to reach an exit before the step limit
             */
            maze.markCurrentCoordinateTryStatus(false);
            return null;
        }
    }

    /**
     * Returns boolean based on if following facts are true:
     * 1. Current tile is not a BLOCK
     * 2. Current coordinates have not been tried yet during the current path attempt
     * 3. Current step count is still within the step limit
     * @param maze
     * @return
     */
    private static boolean isCurrentPositionTraversable(Maze maze) {
        boolean currentTileTraversable = maze.getCurrentTile() != Tile.BLOCK;
        boolean currentCoordinatesNotTried = !maze.currentCoordinatesTried();
        boolean withinStepLimit = maze.getCurrentStepCount() <= maze.getStepLimit();

        return currentTileTraversable && currentCoordinatesNotTried && withinStepLimit;
    }

    /**
     * Returns a Direction pointing towards the path to an EXIT.
     * If current tile is an EXIT tile returns current Direction and marks the maze as solved.
     * If an EXIT fas found down the path then return the Direction pointing towards the next step
     * If an EXIT was not found down the path then return null
     * @param maze
     * @return
     */
    private static Direction getSuccessfulDirection(Maze maze) {
        boolean atExit = maze.getCurrentTile() == Tile.EXIT;

        if (atExit) {
            maze.markAsSolved();
            return maze.getCurrentDirection();
        }

        return findSuccessfulDirection(maze);
    }

    /**
     * Attempts to find an EXIT by trying to go as far as it can through each adjacent tile.
     * If an EXIT was found through any of the Directions then return a Direction pointing towards the next tile down
     * the path towards an EXIT. Will stop iterating as soon as a successful Direction is found.
     * If no EXIT was found then returns null
     * @param maze
     * @return
     */
    private static Direction findSuccessfulDirection(Maze maze) {
        return Arrays.stream(Direction.MOVING_DIRECTIONS)
                .filter(d -> isDirectionSuccessful(maze, d))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a boolean based on if an EXIT was found by going through as far as it can through by starting from an
     * adjacent tile from current position.
     *
     * Will not attempt to go towards the direction if it would result in going over the maze edges.
     *
     * Will update the current position/direction/step count before calling the next recursion of traverseMaze, and then
     * set the position/direction/step count values back to previous after coming back from the next recursion.
     *
     * @param maze
     * @param nextDirection
     * @return
     */
    private static boolean isDirectionSuccessful(Maze maze, Direction nextDirection) {
        if (!wouldGoOverEdge(maze, nextDirection)) {
            Coordinates previousCoordinates = maze.getCurrentCoordinates();
            Direction previousDirection = maze.getCurrentDirection();
            int previousStepCount = maze.getCurrentStepCount();

            Coordinates nextCoordinates = getNextCoordinates(maze, nextDirection);
            maze.updatePositionFields(nextCoordinates, nextDirection, previousStepCount + 1);
            boolean nextDirectionSuccessful = traverseMaze(maze) != null;

            // Traverse back to original position after trying direction
            maze.updatePositionFields(previousCoordinates, previousDirection, previousStepCount);

            return nextDirectionSuccessful;
        }

        return false;
    }

    /**
     * Returns a boolean based on if an adjecent tile towards the given Direction would result in going over one of the
     * edges of the maze.
     * @param maze
     * @param direction not null/INITIAL
     * @return
     */
    private static boolean wouldGoOverEdge(Maze maze, Direction direction) {
        int y = maze.getCurrentYCoordinate();
        int x = maze.getCurrentXCoordinate();

        switch (direction) {
            case UP:
                return y - 1 < 0;
            case RIGHT:
                return x + 1 >= maze.getWidth();
            case DOWN:
                return y + 1 >= maze.getHeight();
            case LEFT:
                return x - 1 < 0;
            default:
                throw new IllegalArgumentException("Unknown/null Direction enum: " + direction);
        }
    }

    /**
     * Gets Coordinates to an adjacent tile towards the given Direction.
     *
     * @param maze
     * @param direction
     * @return
     */
    private static Coordinates getNextCoordinates(Maze maze, Direction direction) {
        return Direction.getNextCoordinates(direction, maze.getCurrentCoordinates());
    }

    /**
     * Updates the solution char matrix based on current Tile
     *
     * If current tile is SPACE, then mark direction.getChar for current coordinates
     * If current tile was not SPACE, then mark currentTile.getChar for current coordinates.
     *
     * @param maze
     * @param direction
     */
    private static void updateSolution(Maze maze, Direction direction) {
        Tile currentTile = maze.getCurrentTile();
        char solutionChar = currentTile == Tile.SPACE
                ? direction.getChar()
                : currentTile.getChar(); // Blocks cannot traversed so either START or END

        maze.markSolutionAtCurrentLocation(solutionChar);
    }
}
