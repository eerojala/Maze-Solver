package com.maze.domain;

public class Maze {
    // Immutable structure related fields
    private final Tile[][] maze;
    private final int height;
    private final int width;
    private final Coordinates startingCoordinates;

    // Progression tracking related fields
    private boolean[][] triedCoordinates;
    private int stepLimit;
    private int currentStepCount;
    private Coordinates currentCoordinates;
    private Direction currentDirection;

    // Solution related fields
    private char[][] solution;
    private int solutionStepCount;
    private boolean solved;

    public Maze(Tile[][] maze) {
        this.maze = maze;
        height = maze.length;
        width = maze[0].length;
        startingCoordinates = findStartingCoordinates();
        resetProgress(0); // Method called to prevent null pointers
    }

    private Coordinates findStartingCoordinates() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (maze[y][x] == Tile.START) {
                    return new Coordinates(y, x);
                }
            }
        }

        throw new IllegalArgumentException("Given maze must have a starting point (marked with '^'");
    }

    public void resetProgress(int stepLimit) {
        triedCoordinates = new boolean[height][width];
        this.stepLimit = stepLimit;
        currentStepCount = 0;
        currentCoordinates = startingCoordinates;
        currentDirection = Direction.INITIAL;

        solution = new char[height][width];
        solutionStepCount = 0;
        solved = false;
    }

    public Tile getCurrentTile() {
        return getTileForCoordinates(currentCoordinates);
    }

    public Tile getTileForCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot get Tile for null coordinates");
        }

        return maze[coordinates.getY()][coordinates.getX()];
    }

    public boolean currentCoordinatesTried() {
        return coordinatesTried(currentCoordinates);
    }

    public boolean coordinatesTried(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot check if null coordinates are tried");
        }

        return triedCoordinates[coordinates.getY()][coordinates.getX()];
    }

    public void markCurrentCoordinatesAsTried() {
        markCoordinatesAsTried(currentCoordinates);
    }

    public void markCoordinatesAsTried(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Null coordinates cannot be marked as tried");
        }

        triedCoordinates[coordinates.getY()][coordinates.getX()] = true;
    }

    public void markSolutionAtCurrentLocation(char solutionChar) {
        markSolutionAt(currentCoordinates, solutionChar);
    }

    public void markSolutionAt(Coordinates coordinates, char solutionChar) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot mark null coordinates as part of the solution");
        }

        solution[coordinates.getY()][coordinates.getX()] = solutionChar;
    }

    public int getCurrentYCoordinate() {
        return currentCoordinates.getY();
    }

    public int getCurrentXCoordinate() {
        return currentCoordinates.getX();
    }

    public void updatePositionFields(Coordinates coordinates, Direction direction, int steps) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot mark current coordinates as null");
        }

        if (direction == null) {
            throw new IllegalArgumentException("Cannot mark current direction as null");
        }

        currentCoordinates = coordinates;
        currentDirection = direction;
        currentStepCount = steps;
    }

    public char getSolutionCharFromCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot get solution char from null coordinates");
        }

        return solution[coordinates.getY()][coordinates.getX()];
    }

    // Basic getters and setters for fields

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getStepLimit() {
        return stepLimit;
    }

    public int getCurrentStepCount() {
        return currentStepCount;
    }

    public Coordinates getCurrentCoordinates() {
        return currentCoordinates;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public int getSolutionStepCount() {
        return solutionStepCount;
    }

    public void setSolutionStepCount(int solutionStepCount) {
        this.solutionStepCount = solutionStepCount;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
