package com.maze;

import java.util.Arrays;

public class MazeSolver {
    private enum Direction {
        UP, RIGHT, DOWN, LEFT;
    }

    private Tile[][] maze;
    private boolean[][] traversedPath;
    private boolean[][] endPath;
    private int mazeWidth;
    private int mazeHeight;
    private int stepLimit;

    public char[][] solveMaze(Tile[][] maze, int stepLimit) {
        /*
         * TODO:
         * Make order of x and y consistent
         * Make solution print more clear
         */
        initFields(maze, stepLimit);
        var start = getStartingCoordinates();
        boolean possible = traverseMaze(start, 0);

        return possible ? getSolution() : null;
    }

    private void initFields(Tile[][] maze, int stepLimit) {
        this.maze = maze;
        mazeWidth = maze[0].length;
        mazeHeight = maze.length;
        traversedPath = new boolean[mazeHeight][mazeWidth]; // booleans are false by default
        endPath = new boolean[mazeHeight][mazeWidth];
        this.stepLimit = stepLimit;
    }

    private Coordinates getStartingCoordinates() {
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                if (maze[i][j] == Tile.START) {
                    return new Coordinates(j, i); // NOTE: j = x and i = y
                }
            }
        }

        throw new IllegalArgumentException("Given maze must have a starting point (marked with '^'");
    }

    private boolean traverseMaze(Coordinates currentCoordinates, int steps) {
        int x = currentCoordinates.getX();
        int y = currentCoordinates.getY();
        Tile currentTile = maze[y][x];

        if (!areCurrentCoordinatesTraversable(currentTile, currentCoordinates, steps)) {
            return false;
        }

        markCoordinatesAsTraversed(currentCoordinates);

        if (foundExit(currentTile, currentCoordinates, steps)) {
            endPath[y][x] = true;
            return true;
        } else {
            return false;
        }
    }

    private boolean areCurrentCoordinatesTraversable(Tile currentTile, Coordinates currentCoordinates, int steps) {
        boolean currentTileTraversable = currentTile != Tile.BLOCK;
        boolean currentCoordinatesNotTraversed = !traversedPath[currentCoordinates.getY()][currentCoordinates.getX()];
        boolean withinStepLimit = steps <= stepLimit;

        return currentTileTraversable && currentCoordinatesNotTraversed && withinStepLimit;
    }

    private void markCoordinatesAsTraversed(Coordinates coordinates) {
        traversedPath[coordinates.getY()][coordinates.getX()] = true;
    }

    private boolean foundExit(Tile currentTile, Coordinates currentCoordinates, int steps) {
        boolean atExit = currentTile == Tile.EXIT;
        boolean foundExitFurther = tryTraverseAllDirections(currentCoordinates, steps);

        return atExit || foundExitFurther;
    }

    private boolean tryTraverseAllDirections(Coordinates current, int steps) {
        return Arrays.stream(Direction.values()).anyMatch(d -> {
            if (!isAtEdge(d, current)) {
                Coordinates next = getNextCoordinates(d, current);
                return traverseMaze(next, steps + 1);
            }

            return false;
        });
    }

    private boolean isAtEdge(Direction direction, Coordinates coordinates) {
        int x = coordinates.getX();
        int y = coordinates.getY();

        switch (direction) {
            case UP:
                return y - 1 <= 0;
            case RIGHT:
                return x + 1 >= mazeWidth;
            case DOWN:
                return y + 1 >= mazeHeight;
            case LEFT:
                return x - 1 <= 0;
            default:
                throw new IllegalArgumentException("Unknown Direction enum: " + direction);
        }
    }

    private Coordinates getNextCoordinates(Direction direction, Coordinates coordinates) {
        int x = coordinates.getX();
        int y = coordinates.getY();

        switch (direction) {
            case UP:
                return new Coordinates(x, y - 1);
            case RIGHT:
                return new Coordinates(x + 1, y);
            case DOWN:
                return new Coordinates(x, y + 1);
            case LEFT:
                return new Coordinates(x - 1, y);
            default:
                throw new IllegalArgumentException("Unknown Direction enum: " + direction);
        }
    }

    private char[][] getSolution() {
        var solution = new char[mazeHeight][mazeWidth];

        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                solution[y][x] = getSolutionChar(x, y);
            }
        }

        return solution;
    }

    private char getSolutionChar(int x, int y) {
        Tile tile = maze[y][x];
        boolean markableEndPathTile =  endPath[y][x] == true && tile == Tile.SPACE;

        return markableEndPathTile ? 'x' : tile.getSymbol();
    }
}
