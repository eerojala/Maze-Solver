package com.maze.domain;

public class Maze {
    // Immutable structure related fields
    private final Tile[][] maze;
    private final int height;
    private final int width;
    private final Coordinates startingCoordinates;

    // Progression related fields
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
        resetProgress(0); // Method (re-)initializes other fields
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
        currentDirection = null;

        solution = new char[height][width];
        solutionStepCount = 0;
        solved = false;
    }

    public Tile getCurrentTile() {
        return getTileForCoordinates(currentCoordinates);
    }

    public Tile getTileForCoordinates(Coordinates coordinates) {
        return maze[coordinates.getY()][coordinates.getX()];
    }

    public boolean currentCoordinatesTried() {
        return coordinatesTried(currentCoordinates);
    }

    public boolean coordinatesTried(Coordinates coordinates) {
        return triedCoordinates[coordinates.getY()][coordinates.getX()];
    }

    public void markCurrentCoordinatesAsTried() {
        markCoordinatesAsTried(currentCoordinates);
    }

    public void markCoordinatesAsTried(Coordinates coordinates) {
        triedCoordinates[coordinates.getY()][coordinates.getX()] = true;
    }

    public void markSolutionAtCurrentLocation(char solutionChar) {
        markSolutionAt(currentCoordinates, solutionChar);
    }

    public void markSolutionAt(Coordinates coordinates, char solutionChar) {
        solution[coordinates.getY()][coordinates.getX()] = solutionChar;
    }

    public int getCurrentYCoordinate() {
        return currentCoordinates.getY();
    }

    public int getCurrentXCoordinate() {
        return currentCoordinates.getX();
    }

    public void updatePositionFields(Coordinates coordinates, Direction direction, int steps) {
        currentCoordinates = coordinates;
        currentDirection = direction;
        currentStepCount = steps;
    }

    public char getSolutionCharFromCoordinates(Coordinates coordinates) {
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
