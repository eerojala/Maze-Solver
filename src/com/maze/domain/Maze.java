package com.maze.domain;

import java.util.*;

public class Maze {
    private static final List<Integer> LIMITS = List.of(20, 150, 200);

    // Immutable structure related fields
    private final Tile[][] maze;
    private final int height;
    private final int width;
    private final Coordinates startingCoordinates;

    // Overall progression tracking related fields
    private int[][] stepTracker;
    private Direction[][] directionTracker;
    private int stepLimitIndex;

    // Current progression iteration tracking related fields
    private List<CoordinatesAndDirection> currentCoordinatesAndDirections;
    private int currentStepCount;

    // Solution related fields
    private boolean solved;
    private Map<Coordinates, Direction> solutionPath;

    public Maze(Tile[][] maze) {
        this.maze = maze;
        height = maze.length;
        width = maze[0].length;
        startingCoordinates = findStartingCoordinates();

        this.stepTracker = new int[height][width];
        Arrays.stream(stepTracker).forEach(row -> Arrays.fill(row, -1)); // Fill matrix with value -1
        directionTracker = new Direction[height][width];
        stepLimitIndex = 0;

        currentCoordinatesAndDirections = List.of(new CoordinatesAndDirection(startingCoordinates, Direction.INITIAL));
        currentStepCount = 0;

        solved = false;
        solutionPath = new HashMap<>();
    }

    /**
     * Finds and returns the starting coordinates (=coordinates where the START tile is located at)
     *
     * @return
     *
     * @throws IllegalArgumentException if the maze does not contain exactly one starting point
     */
    private Coordinates findStartingCoordinates() {
        Coordinates startingCoordinates = null;
        int startTilesFound = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (maze[y][x] == Tile.START) {
                    startTilesFound++;
                    startingCoordinates = new Coordinates(y, x);
                }
            }
        }

        if (startTilesFound != 1) {
            throw new IllegalArgumentException("Given maze must have exactly one starting point (marked with '^')");
        }

        return startingCoordinates;
    }

    public boolean overCurrentLimit() {
        return currentStepCount > LIMITS.get(stepLimitIndex);
    }

    public int getCurrentStepLimit() {
        return LIMITS.get(stepLimitIndex);
    }

    public boolean incrementCurrentLimit() {
        if (stepLimitIndex >= LIMITS.size() - 1) {
            return false;
        }

        stepLimitIndex++;

        return true;
    }

    /**
     * Returns the Tile of the given coordinates
     *
     * @param coordinates not null
     * @return
     */
    public Tile getTileForCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot get Tile for null coordinates");
        }

        if (!areCoordinatesInBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot get Tile for out-of-bounds coordinates " + coordinates);
        }

        return maze[coordinates.getY()][coordinates.getX()];
    }

    public boolean areCoordinatesInBounds(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot check if null coordinates are in bounds");
        }

        int y = coordinates.getY();
        int x = coordinates.getX();
        boolean tooUp = y < 0;
        boolean tooRight = x >= width;
        boolean tooDown = y >= height;
        boolean tooLeft = x < 0;

        return !(tooUp || tooRight || tooDown || tooLeft);
    }

    public int getStepCountForCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot get step count for null coordinates");
        }

        if (!areCoordinatesInBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot get step count for out-of-bounds coordinates " + coordinates);
        }

        return stepTracker[coordinates.getY()][coordinates.getX()];
    }

    public void markCurrentStepCountForCoordinates(Coordinates coordinates) {
        markStepCountForCoordinates(coordinates, currentStepCount);
    }

    public void markStepCountForCoordinates(Coordinates coordinates, int stepCount) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot mark step count for null coordinates");
        }

        if (!areCoordinatesInBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot mark step count for out-of-bounds coordinates " + coordinates);
        }

        stepTracker[coordinates.getY()][coordinates.getX()] = stepCount;
    }

    public void markDirectionForCoordinates(Coordinates coordinates, Direction direction) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot mark direction for null coordinates");
        }

        if (!areCoordinatesInBounds(coordinates)) {
            throw new NullPointerException("Cannot mark direction for null coordinates");
        }

        directionTracker[coordinates.getY()][coordinates.getX()] = direction;
    }

    public Direction getDirectionForCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot get direction for null coordinates");
        }

        if (!areCoordinatesInBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot get direction of out-of-bounds coordinates " + coordinates);
        }

        return directionTracker[coordinates.getY()][coordinates.getX()];
    }

    public void updateSolutionPath(Coordinates coordinates, Direction direction) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot put null coordinates to the solution path");
        }

        if (direction == null) {
            // IAE instead of NPE because only passing the argument and not actually calling it's methods.
            throw new IllegalArgumentException("Cannot put null direction to the solution path");
        }

        solutionPath.put(coordinates, direction);
    }

    public Direction getDirectionFromSolutionPath(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot get direction from solution path for null coordinates");
        }

        return solutionPath.get(coordinates);
    }

    // Basic getters and setters for fields

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getCurrentStepCount() {
        return currentStepCount;
    }

    public boolean isSolved() {
        return solved;
    }

    public Tile[][] getMaze() {
        return maze;
    }

    public Coordinates getStartingCoordinates() {
        return startingCoordinates;
    }

    public int[][] getStepTracker() {
        return stepTracker;
    }

    public void setStepTracker(int[][] stepTracker) {
        this.stepTracker = stepTracker;
    }

    public Direction[][] getDirectionTracker() {
        return directionTracker;
    }

    public void setDirectionTracker(Direction[][] directionTracker) {
        this.directionTracker = directionTracker;
    }

    public void setCurrentStepCount(int currentStepCount) {
        this.currentStepCount = currentStepCount;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public List<CoordinatesAndDirection> getCurrentCoordinatesAndDirections() {
        return currentCoordinatesAndDirections;
    }

    public void setCurrentCoordinatesAndDirections(List<CoordinatesAndDirection> currentCoordinatesAndDirections) {
        this.currentCoordinatesAndDirections = currentCoordinatesAndDirections;
    }

    public Map<Coordinates, Direction> getSolutionPath() {
        return solutionPath;
    }

    public void setSolutionPath(Map<Coordinates, Direction> solutionPath) {
        this.solutionPath = solutionPath;
    }
}
