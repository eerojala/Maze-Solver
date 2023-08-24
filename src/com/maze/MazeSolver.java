package com.maze;

import java.util.ArrayList;
import java.util.Objects;

public class MazeSolver {
    private enum Direction {
        UP, RIGHT, DOWN, LEFT;
    }

    private Tile[][] maze;
    private int mazeWidth;
    private int mazeHeight;
    private boolean[][] traversedPath;
    private boolean[][] endPath;

    public char[][] solveMaze(Tile[][] maze) {
        /*
         * TODO:
         * Make order of x and y consistent
         * Take limits into account
         * Make solution print more clear
         */
        initFields(maze);
        var start = getStartingCoordinates();
        boolean possible = traverseMaze(start);

        return possible ? getSolution() : null;
    }

    private void initFields(Tile[][] maze) {
        this.maze = maze;
        mazeWidth = maze[0].length;
        mazeHeight = maze.length;
        traversedPath = new boolean[mazeHeight][mazeWidth]; // booleans are false by default
        endPath = new boolean[mazeHeight][mazeWidth];
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

    private boolean traverseMaze(Coordinates current) {
        int x = current.getX();
        int y = current.getY();
        Tile currentTile = maze[y][x];

        if (currentTile == Tile.BLOCK || isTileTraversed(current)) {
            return false;
        }

        markTileAsTraversed(current);

        if (currentTile == Tile.EXIT || tryAllDirections(current)) {
            endPath[y][x] = true;
            return true;
        } else {
            return false;
        }
    }

    private boolean isTileTraversed(Coordinates coordinates) {
        return traversedPath[coordinates.getY()][coordinates.getX()];
    }

    private boolean tryAllDirections(Coordinates current) {
        // Try in order of UP, RIGHT, DOWN, LEFT
        for (Direction d: Direction.values()) {
            if (!isAtEdge(d, current)) {
                Coordinates next = getNextCoordinates(d, current);

                if (traverseMaze(next)) {
                    return true;
                }
            }
        }

        return false;
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

    private void markTileAsTraversed(Coordinates coordinates) {
        traversedPath[coordinates.getY()][coordinates.getX()] = true;
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
