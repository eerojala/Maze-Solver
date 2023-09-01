package com.maze.util;

import com.maze.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MazeSolver {
    private static final int[] LIMITS = { 20, 150, 200 };

    private MazeSolver() {
        // Private empty constructor for static method class
    }

    public static void solveMaze(Maze maze) {
        if (maze == null) {
            throw new NullPointerException("Solvable maze cannot be null");
        }

        if (isStepCountOverMaxLimit(maze)) {
            return;
        }

        Coordinates exitCoordinates = markCurrentStepCountsAndDirections(maze);

        if (exitCoordinates == null) {
            setupMazeForNextIteration(maze);
            solveMaze(maze);
        } else {
            maze.setSolved(true);
            markSolution(maze, exitCoordinates, null);
        }
    }

    private static boolean isStepCountOverMaxLimit(Maze maze) {
        if (!maze.overCurrentLimit()) {
            return false;
        }

        boolean limitIncremented = maze.incrementCurrentLimit();

        return limitIncremented ? isStepCountOverMaxLimit(maze) : false;
    }

    private static Coordinates markCurrentStepCountsAndDirections(Maze maze) {
        for (var cad : maze.getCurrentCoordinatesAndDirections()) {
            Coordinates coordinates = cad.getCoordinates();
            maze.markCurrentStepCountForCoordinates(coordinates);

            Direction direction = cad.getDirection();
            maze.markDirectionForCoordinates(coordinates, direction);

            if (areExitCoordinates(maze, coordinates)) {
                return coordinates;
            }
        }

        return null;
    }

    private static boolean areExitCoordinates(Maze maze, Coordinates coordinates) {
        return maze.getTileForCoordinates(coordinates) == Tile.EXIT;
    }

    private static void setupMazeForNextIteration(Maze maze) {
        maze.setCurrentCoordinatesAndDirections(getValidAdjacentCoordinatesAndDirections(maze));
        maze.setCurrentStepCount(maze.getCurrentStepCount() + 1);
    }

    private static List<CoordinatesAndDirection> getValidAdjacentCoordinatesAndDirections(Maze maze) {
        return maze.getCurrentCoordinatesAndDirections().stream()
                .map(CoordinatesAndDirection::getCoordinates)
                .map(c -> getValidAdjacentCoordinatesAndDirectionsForCoordinates(maze, c))
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());
    }

    private static List<CoordinatesAndDirection> getValidAdjacentCoordinatesAndDirectionsForCoordinates(
            Maze maze, Coordinates coordinates) {
        return Arrays.stream(Direction.MOVING_DIRECTIONS)
                .map(d -> createAdjacentCoordinatesAndDirection(coordinates, d))
                .filter(cad -> areCoordinatesValid(maze, cad.getCoordinates()))
                .collect(Collectors.toList());
    }

    private static CoordinatesAndDirection createAdjacentCoordinatesAndDirection(
            Coordinates coordinates, Direction direction) {
        return new CoordinatesAndDirection(Direction.getNextCoordinates(direction, coordinates), direction);
    }

    private static boolean areCoordinatesValid(Maze maze, Coordinates coordinates) {
        if (!maze.areCoordinatesInBounds(coordinates)) {
            return false;
        }

        boolean notTried = maze.getStepCountForCoordinates(coordinates) == -1;
        boolean walkable = maze.getTileForCoordinates(coordinates) != Tile.BLOCK;

        return notTried && walkable;
    }

    private static void markSolution(Maze maze, Coordinates coordinates, Direction previousDirection) {
        if (maze.getTileForCoordinates(coordinates) == Tile.START) {
            return;
        }

        if (previousDirection != null) {
            maze.updateSolutionPath(coordinates, previousDirection);
        }

        Direction currentDirection = maze.getDirectionForCoordinates(coordinates);
        /*
         * NOTE: This method is traversing the solution path backwards, from the exit back to the entrance.
         * Therefore the variables are named "previousDirection" and "nextCoordinates" to reflect the method traversal
         * order and not the actual solution path order.
         */
        Coordinates nextCoordinates = Direction.getPreviousCoordinates(currentDirection, coordinates);
        markSolution(maze, nextCoordinates, currentDirection);
    }
}
