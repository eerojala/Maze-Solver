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
            throw new IllegalArgumentException("Solvable maze cannot be null");
        }

        if (isStepCountOverMaxLimit(maze)) {
            return;
        }

        boolean exitFound = markCurrentStepCountsAndDirections(maze);

        if (exitFound) {
            maze.setSolved(true);
            // TODO: Mark solution path
        } else {
            setupMazeForNextIteration(maze);
            solveMaze(maze);
        }
    }

    private static boolean isStepCountOverMaxLimit(Maze maze) {
        if (!maze.overCurrentLimit()) {
            return false;
        }

        boolean limitIncremented = maze.incrementCurrentLimit();

        return limitIncremented ? isStepCountOverMaxLimit(maze) : false;
    }

    private static boolean markCurrentStepCountsAndDirections(Maze maze) {
        for (var cad : maze.getCurrentCoordinatesAndDirections()) {
            Coordinates coordinates = cad.getCoordinates();
            maze.markCurrentStepCountForCoordinates(coordinates);

            Direction direction = cad.getDirection();
            maze.markDirectionForCoordinates(coordinates, direction);

            if (areExitCoordinates(maze, coordinates)) {
                return true;
            }
        }

        return false;
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

    private static void markMazeAsSolved(Maze maze) {
        maze.setSolved(true);
    }
}
