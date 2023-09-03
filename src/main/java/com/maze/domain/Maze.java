package com.maze.domain;

import java.util.*;

public class Maze {
    private static final List<Integer> LIMITS = List.of(20, 150, 200);

    // Structure related fields
    private final Tile[][] maze;
    private final int height;
    private final int width;

    // Overall progression tracking related fields
    private final boolean[][] coordinatesCheckStatus;
    private final Direction[][] directionTracker;
    private int stepLimitIndex;

    // Current progression iteration tracking related fields
    private List<CoordinatesAndDirection> currentCoordinatesAndDirections;
    private int currentStepCount;

    // Solution related fields
    private boolean solved;
    private final Map<Coordinates, Direction> solutionPath;

    public Maze(Tile[][] maze) {
        this.maze = maze;
        height = maze.length;
        width = maze[0].length;


        this.coordinatesCheckStatus = new boolean[height][width];
        directionTracker = new Direction[height][width];
        stepLimitIndex = 0;

        Coordinates startingCoordinates = findStartingCoordinates();
        currentCoordinatesAndDirections = List.of(new CoordinatesAndDirection(startingCoordinates, Direction.INITIAL));
        currentStepCount = 0;

        solved = false;
        solutionPath = new HashMap<>();
    }

    /**
     * Finds and returns the starting coordinates (=coordinates where the START tile is located at)
     *
     * @return the starting coordinates of the maze
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

    /**
     * Checks if current step count is over the current limit
     *
     * @return true if current step count is over the current limit, false if not
     */
    public boolean overCurrentLimit() {
        return currentStepCount > LIMITS.get(stepLimitIndex);
    }

    /**
     * Returns current step limit of the maze
     * @return current step limit
     */
    public int getCurrentStepLimit() {
        return LIMITS.get(stepLimitIndex);
    }

    /**
     * Increases current limit unless the current limit is already at maximum
     *
     * @return true if limit was increased, false if not (limit was already at maximum)
     */
    public boolean increaseCurrentLimit() {
        if (stepLimitIndex >= LIMITS.size() - 1) {
            return false;
        }

        stepLimitIndex++;

        return true;
    }

    /**
     * Returns the Tile of the given coordinates
     *
     * @param coordinates not null and not out-of-bounds
     * @return Tile of the given coordinates
     */
    public Tile getTileForCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot get Tile for null coordinates");
        }

        if (areCoordinatesOutOfBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot get Tile for out-of-bounds coordinates " + coordinates);
        }

        return maze[coordinates.getY()][coordinates.getX()];
    }

    /**
     * Checks if given coordinates would be out-of-bounds for the maze
     *
     * @param coordinates not null
     * @return true if Coordinates are out-of-bounds, false if not
     */
    public boolean areCoordinatesOutOfBounds(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot check if null coordinates are in bounds");
        }

        int y = coordinates.getY();
        int x = coordinates.getX();
        boolean tooUp = y < 0;
        boolean tooRight = x >= width;
        boolean tooDown = y >= height;
        boolean tooLeft = x < 0;

        return tooUp || tooRight || tooDown || tooLeft;
    }

    /**
     * Checks if given coordinates have already been checked.
     *
     * @param coordinates not null and not out-of-bounds
     * @return true if given coordinates have already been checked, false if not
     */
    public boolean areCoordinatesChecked(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot check if null coordinates are visited");
        }

        if (areCoordinatesOutOfBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot check if out-of-bounds coordinates are visited " + coordinates);
        }

        return coordinatesCheckStatus[coordinates.getY()][coordinates.getX()];
    }

    /**
     * Marks given coordinates as checked.
     *
     * @param coordinates not null and not out-of-bounds
     */
    public void markCoordinatesAsChecked(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot mark null coordinates as visited");
        }

        if (areCoordinatesOutOfBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot mark out-of-bound coordinates as visited" + coordinates);
        }

        coordinatesCheckStatus[coordinates.getY()][coordinates.getX()] = true;
    }

    /**
     * Marks the given direction for the given coordinates.
     * The direction will be used for displaying the solution path in file/console print if maze is solvable.
     *
     * @param coordinates not null and not out out of bounds
     * @param direction from the previous tile
     */
    public void markDirectionForCoordinates(Coordinates coordinates, Direction direction) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot mark direction for null coordinates");
        }

        if (areCoordinatesOutOfBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot mark direction for null coordinates");
        }

        directionTracker[coordinates.getY()][coordinates.getX()] = direction;
    }

    /**
     * Returns the Direction which has been stored for the given Coordinates. If no direction has been saved for the
     * coordinates then returns null
     * @param coordinates not null and not out-of-bounds
     * @return Direction stored for the given Coordinates
     */
    public Direction getDirectionForCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Cannot get direction for null coordinates");
        }

        if (areCoordinatesOutOfBounds(coordinates)) {
            throw new IndexOutOfBoundsException("Cannot get direction of out-of-bounds coordinates " + coordinates);
        }

        return directionTracker[coordinates.getY()][coordinates.getX()];
    }

    /**
     * Updates the given Coordinates and Direction towards the next coordinates in the solution path.
     *
     * @param coordinates not null
     * @param direction not null
     */
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

    /**
     * Gets the Direction pointing towards the next Coordinates in the solution path for the given coordinates. Returns
     * null if the given Coordinates are not part of the solution path.
     * @param coordinates not null
     * @return Direction pointing towards the next Coordinates in the solution path
     */
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

    public List<CoordinatesAndDirection> getCurrentCoordinatesAndDirections() {
        return currentCoordinatesAndDirections;
    }

    public void setCurrentCoordinatesAndDirections(List<CoordinatesAndDirection> currentCoordinatesAndDirections) {
        this.currentCoordinatesAndDirections = currentCoordinatesAndDirections;
    }

    public int getCurrentStepCount() {
        return currentStepCount;
    }

    public void setCurrentStepCount(int currentStepCount) {
        this.currentStepCount = currentStepCount;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}