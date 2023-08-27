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

        maze.markCurrentCoordinatesAsTried();
        Direction nextSuccessfulDirection = getSuccessfulDirection(maze);

        if (nextSuccessfulDirection != null) {
            updateSolution(maze, nextSuccessfulDirection);

            return maze.getCurrentDirection();
        } else {
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
        return Arrays.stream(Direction.values())
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
                return y - 1 <= 0;
            case RIGHT:
                return x + 1 >= maze.getWidth();
            case DOWN:
                return y + 1 >= maze.getHeight();
            case LEFT:
                return x - 1 <= 0;
            default:
                throw new IllegalArgumentException("Unknown Direction enum: " + direction);
        }
    }

    private static Coordinates getNextCoordinates(Maze maze, Direction direction) {
        int y = maze.getCurrentYCoordinate();
        int x = maze.getCurrentXCoordinate();

        switch (direction) {
            case UP:
                return new Coordinates(y - 1, x);
            case RIGHT:
                return new Coordinates(y, x + 1);
            case DOWN:
                return new Coordinates(y + 1, x);
            case LEFT:
                return new Coordinates(y,x - 1);
            default:
                throw new IllegalArgumentException("Unknown Direction enum: " + direction);
        }
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
