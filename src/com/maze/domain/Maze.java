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

    /**
     * Resets the progression status of the maze back to default and updates the stepLimit to the given amount
     *
     * @param stepLimit
     */
    public void resetProgress(int stepLimit) {
        triedCoordinates = new boolean[height][width];
        this.stepLimit = stepLimit;
        updatePositionFields(startingCoordinates, Direction.INITIAL, 0 );

        solution = new char[height][width];
        solutionStepCount = 0;
        solved = false;
    }

    /**
     * Returns the Tile of the current coordinates
     *
     * @return
     */
    public Tile getCurrentTile() {
        return getTileForCoordinates(currentCoordinates);
    }

    /**
     * Returns the Tile of the given coordinates
     *
     * @param coordinates not null
     * @return
     */
    public Tile getTileForCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot get Tile for null coordinates");
        }

        return maze[coordinates.getY()][coordinates.getX()];
    }

    /**
     * Returns boolean based on if the current coordinates have been tried
     *
     * @return
     */
    public boolean currentCoordinatesTried() {
        return coordinatesTried(currentCoordinates);
    }

    /**
     * Returns boolean based on if the given coordinates have been tired
     *
     * @param coordinates not null
     * @return
     */
    public boolean coordinatesTried(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot check if null coordinates are tried");
        }

        return triedCoordinates[coordinates.getY()][coordinates.getX()];
    }

    /**
     * Marks the current coordinates as tried/untried based on the given boolean
     *
     * @param tried
     */
    public void markCurrentCoordinateTryStatus(boolean tried) {
            markCoordinateTryStatus(currentCoordinates, tried);
    }

    /**
     * Marks the given coordinates as tried/untried based on the given boolean
     *
     * @param coordinates not null
     * @param tried
     */
    public void markCoordinateTryStatus(Coordinates coordinates, boolean tried) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot update try status for null coordinates");
        }

        triedCoordinates[coordinates.getY()][coordinates.getX()] = tried;
    }

    /**
     * Marks the current coordinates as part of the final solution
     *
     * @param solutionChar
     */
    public void markSolutionAtCurrentLocation(char solutionChar) {
        markSolutionAt(currentCoordinates, solutionChar);
    }

    /**
     * Marks the given coordinates as part of the final solution with the given char
     *
     * @param coordinates not null
     * @param solutionChar
     */
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

    /**
     * Updates current coordinates, direction and step count.
     * @param coordinates not null
     * @param direction not null
     * @param steps
     */
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

    /**
     * Gets a char representing the Tile on the given coordinates on the solution matrix.
     *
     * If the coordinates are a part of the solution and the Tile at the coordinates is Tile.START or TILE.Exit, then
     * returns value of Tile.getChar() (i.e. '^' or 'E').
     *
     * If the coordinates are a part of the solution and the Tile at the coordinates is TILE.SPACE, then returns the
     * value of Direction.getChar() based on which direction the path turned towards the exit in the solution (i.e.
     * '↑', '→', '↓' or '←').
     *
     * If the coordinates are not a part of the solution, then returns a null char.
     *
     * @param coordinates not null
     * @return
     */
    public char getSolutionCharFromCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot get solution char from null coordinates");
        }

        return solution[coordinates.getY()][coordinates.getX()];
    }

    /**
     * Mark the maze as solved by setting solved = true and the current step count as the solution step count.
     *
     * NOTE: Stepping into the exit tile is counted into the solution step amount
     */
    public void markAsSolved() {
        solved = true;
        solutionStepCount = currentStepCount;
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

    public boolean isSolved() {
        return solved;
    }
}
