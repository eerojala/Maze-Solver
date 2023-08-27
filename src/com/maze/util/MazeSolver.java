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
             * If it was not possible to reach an exit within the step limit by trying to traverse all directions from
             * this tile then mark this tile as untried before returning.
             *
             * This is done so that this same tile can be tried later through a different route which might be faster
             * and thus maybe able to reach an exit before the step limit
             */
            maze.markCurrentCoordinateTryStatus(false);
            return null;
        }
    }

    private static boolean isCurrentPositionTraversable(Maze maze) {
        boolean currentTileTraversable = maze.getCurrentTile() != Tile.BLOCK;
        boolean currentCoordinatesNotTried = !maze.currentCoordinatesTried();
        boolean withinStepLimit = maze.getCurrentStepCount() <= maze.getStepLimit();

        return currentTileTraversable && currentCoordinatesNotTried && withinStepLimit;
    }

    private static Direction getSuccessfulDirection(Maze maze) {
        boolean atExit = maze.getCurrentTile() == Tile.EXIT;

        if (atExit) {
            setSolved(maze);
            return maze.getCurrentDirection();
        }

        return findSuccessfulDirection(maze);
    }

    private static Direction findSuccessfulDirection(Maze maze) {
        return Arrays.stream(Direction.MOVING_DIRECTIONS)
                .filter(d -> isDirectionSuccessful(maze, d))
                .findFirst()
                .orElse(null);
    }

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

    private static Coordinates getNextCoordinates(Maze maze, Direction direction) {
        return Direction.getNextCoordinates(direction, maze.getCurrentCoordinates());
    }

    private static void setSolved(Maze maze) {
        maze.setSolved(true);
        maze.setSolutionStepCount(maze.getCurrentStepCount()); // Stepping into exit is counted
    }

    private static void updateSolution(Maze maze, Direction direction) {
        Tile currentTile = maze.getCurrentTile();
        char solutionChar = currentTile == Tile.SPACE
                ? direction.getChar()
                : currentTile.getChar(); // Blocks cannot traversed so either START or END

        maze.markSolutionAtCurrentLocation(solutionChar);
    }
}
