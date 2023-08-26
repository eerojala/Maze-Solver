package com.maze;

import java.util.Arrays;

public class MazeSolver {
    private static final int[] LIMITS = { 20, 150, 200 };

    private Tile[][] maze;
    private boolean[][] triedCoordinates;
    private char[][] solution;
    private int mazeWidth;
    private int mazeHeight;
    private int stepLimit;

    public char[][] solveMaze(Tile[][] maze, int stepLimit) {
        initFields(maze, stepLimit);
        var start = getStartingCoordinates();
        boolean possible = traverseMaze(start, Direction.INITIAL, 0) != null;

        return possible ? solution : null;
    }

    private void initFields(Tile[][] maze, int stepLimit) {
        this.maze = maze;
        mazeWidth = maze[0].length;
        mazeHeight = maze.length;
        triedCoordinates = new boolean[mazeHeight][mazeWidth]; // booleans are false by default
        solution = new char[mazeHeight][mazeWidth];
        this.stepLimit = stepLimit;
    }

    private Coordinates getStartingCoordinates() {
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                if (maze[y][x] == Tile.START) {
                    return new Coordinates(y, x);
                }
            }
        }

        throw new IllegalArgumentException("Given maze must have a starting point (marked with '^'");
    }

    private Direction traverseMaze(Coordinates currentCoordinates, Direction currentDirection, int steps) {
        Tile currentTile = getTileForCoordinates(currentCoordinates);

        if (!areCurrentCoordinatesTraversable(currentTile, currentCoordinates, steps)) {
            return null;
        }

        markCoordinatesAsTried(currentCoordinates);
        Direction nextSuccessfulDirection =
                getSuccessfulDirection(currentTile, currentDirection, currentCoordinates, steps);

        if (nextSuccessfulDirection != null) {
            markSolutionSymbol(currentCoordinates, currentTile, nextSuccessfulDirection);
            return currentDirection;
        } else {
            return null;
        }
    }

    private Tile getTileForCoordinates(Coordinates coordinates) {
        return maze[coordinates.getY()][coordinates.getX()];
    }

    private boolean areCurrentCoordinatesTraversable(Tile currentTile, Coordinates currentCoordinates, int steps) {
        boolean currentTileTraversable = currentTile != Tile.BLOCK;
        boolean currentCoordinatesNotTried = !areCoordinatesTried(currentCoordinates);
        boolean withinStepLimit = steps <= stepLimit;

        return currentTileTraversable && currentCoordinatesNotTried && withinStepLimit;
    }

    private boolean areCoordinatesTried(Coordinates coordinates) {
        return triedCoordinates[coordinates.getY()][coordinates.getX()];
    }

    private void markCoordinatesAsTried(Coordinates coordinates) {
        triedCoordinates[coordinates.getY()][coordinates.getX()] = true;
    }

    private Direction getSuccessfulDirection(
            Tile currentTile,
            Direction currentDirection,
            Coordinates currentCoordinates,
            int steps) {
        boolean atExit = currentTile == Tile.EXIT;

        return atExit ? currentDirection : findSuccessfulDirection(currentCoordinates, steps);
    }

    private Direction findSuccessfulDirection(Coordinates currentCoordinates, int steps) {
        return Arrays.stream(Direction.MOVING_DIRECTIONS)
                .filter(d -> isSuccessfulDirection(d, currentCoordinates, steps))
                .findFirst()
                .orElse(null);
    }

    private boolean isSuccessfulDirection(Direction direction, Coordinates currentCoordinates, int steps) {
        if (!isAtEdge(direction, currentCoordinates)) {
            Coordinates next = getNextCoordinates(direction, currentCoordinates);
            return traverseMaze(next, direction,steps + 1) != null;
        }

        return false;
    }

    private boolean isAtEdge(Direction direction, Coordinates coordinates) {
        int y = coordinates.getY();
        int x = coordinates.getX();

        switch (direction) {
            case UP:
                return y - 1 <= 0;
            case RIGHT:
                return x + 1 >= mazeWidth;
            case DOWN:
                return y + 1 >= mazeHeight;
            case LEFT:
                return x - 1 <= 0;
            default: // INITIAL
                throw new IllegalArgumentException("Only moving directions allowed");
        }
    }

    private Coordinates getNextCoordinates(Direction direction, Coordinates coordinates) {
        int y = coordinates.getY();
        int x = coordinates.getX();

        switch (direction) {
            case UP:
                return new Coordinates(y - 1, x);
            case RIGHT:
                return new Coordinates(y, x + 1);
            case DOWN:
                return new Coordinates(y + 1, x);
            case LEFT:
                return new Coordinates(y,x - 1);
            default: // INITIAL
                throw new IllegalArgumentException("Only moving directions allowed");
        }
    }

    private void markSolutionSymbol(Coordinates coordinates, Tile currentTile, Direction direction) {
        char solutionSymbol = currentTile == Tile.SPACE
                ? direction.getSymbol()
                : currentTile.getSymbol(); // Blocks cannot traversed so either Start or End
        solution[coordinates.getY()][coordinates.getX()] = solutionSymbol;
    }
}
