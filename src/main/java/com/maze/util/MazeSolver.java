package com.maze.util;

import com.maze.domain.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MazeSolver {
    private MazeSolver() {
        // Private empty constructor for static method class
    }

    /**
     * The main Maze solving algorithm, designed to run recursively.
     *
     * Logic is that starting from the starting Coordinates each adjacent Coordinates will be marked as tried as well
     * as their direction from the original Starting coordinate.
     * The process will then be repeated for each of these adjacent Coordinates, and further on their adjacent
     * Coordinates, and so on, until an exit has been found or all of the tiles have been checked.
     *
     * The algorithm also keeps track of the distance to the starting Coordinates from the current iteration of
     * Coordinates. If the distance increases over the maximum limit (currently hardcoded as 200), the method will
     * cut off prematurely and thus being unable to find an exit. Along with the maximum limit there are also prior
     * sub-limits (currently hardcoded as 20 and 150) which will be displayed in the result print if an exit was able
     * to be found within that sub-limit (or the maximum limit if an exit was not able to be found within the sub-limits
     * but was able to be found within the maximum limit)
     *
     * If an exit was able to be found within the maximum limit, the Maze will be marked as solved
     * (maze.IsSolved() == true) and the Coordinates as well as the Directions taken for the successful exit path will
     * be saved into the Maze (maze.getSolutionPath).
     *
     * If an exit was unable to be found within the maximum limit, the method will exit without modifying the Maze
     * solved -flag or the solution path.
     *
     * @param maze not null
     */
    public static void solveMaze(Maze maze) {
        if (maze == null) {
            throw new NullPointerException("Solvable maze cannot be null");
        }

        if (isStepCountOverMaxLimit(maze)) {
            return;
        }

        Coordinates exitCoordinates = markCurrentStepCountsAndDirections(maze);

        if (exitCoordinates == null) {
            setupMazeForNextRecursion(maze);

            if (maze.getCurrentCoordinatesAndDirections().isEmpty()) {
                // Could not find any more traversable coordinates;
                return;
            }

            solveMaze(maze);
        } else {
            maze.setSolved(true);
            markSolution(maze, exitCoordinates, null);
        }
    }

    /**
     * Checks if the current step count is over the maximum limit.
     * Method flow:
     *
     * 1. Return false if current step count is below or the same as the current Maze step limit
     * 2. If did not return in previous step then try to increment the current limit to the next sub-limit
     * 3. If was able to increase the current limit to the next sub-limit, return back to step 1.
     * 4. If was unable to increase the current limit to the next sub-limit (i.e. the the current limit is the maximum
     *    limit), return true
     *
     * @param maze current Maze in the solving algorithm
     * @return true if current step count is over the maximum limit, false if not
     */
    private static boolean isStepCountOverMaxLimit(Maze maze) {
        if (!maze.overCurrentLimit()) {
            return false;
        }

        boolean limitIncremented = maze.increaseCurrentLimit();

        return !limitIncremented || isStepCountOverMaxLimit(maze);
    }

    /**
     * Marks the current Coordinates as tried as well as the Directions in which the current Coordinates are from the
     * previous Coordinates.
     *
     * If one of the current Coordinates is an exit, will immediately stop execution and return the Coordinates to the
     * exit.
     *
     * @param maze current Maze in the solving algorithm.
     * @return If any of the Coordinates is an exit, returns those Coordinates. Null otherwise.
     */
    private static Coordinates markCurrentStepCountsAndDirections(Maze maze) {
        for (var pair : maze.getCurrentCoordinatesAndDirections()) {
            Coordinates coordinates = pair.getLeft();
            maze.markCoordinatesAsChecked(coordinates);

            Direction direction = pair.getRight();
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

    /**
     * Increments the current step count by one, and sets the currently adjacent valid Coordinates as the Coordinates to
     * be checked next as well as their Directions from the current Coordinates.
     * from the current Coordinates.
     *
     * @param maze current Maze in the solving algorithm.
     */
    private static void setupMazeForNextRecursion(Maze maze) {
        maze.setCurrentCoordinatesAndDirections(getValidAdjacentCoordinatesAndDirections(maze));
        maze.setCurrentStepCount(maze.getCurrentStepCount() + 1);
    }

    /**
     * Returns a List of Pair<Coordinates, Directions> of Coordinates adjacent to all of the current Coordinates as
     * well their Directions from all of the current Coordinates.
     *
     * @param maze Current Maze in the solving algorithm.
     * @return List of Pairs of Coordinates adjacent to all of the current Coordinates as well their Directions from
     *         all of the current Coordinates.
     */
    private static List<Pair<Coordinates, Direction>> getValidAdjacentCoordinatesAndDirections(Maze maze) {
        return maze.getCurrentCoordinatesAndDirections().stream()
                .map(Pair::getLeft)
                .map(c -> getValidAdjacentCoordinatesAndDirectionsForCoordinates(maze, c))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Returns a List of Pair<Coordinates, Direction> of Coordinates adjacent to the given Coordinates as well as
     * their Direction from the given Coordinates.
     * @param maze Current Maze in the solving algorithm
     * @param coordinates Coordinates.
     * @return List of Pair<Coordinates, Direction> of Coordinates adjacent to the given Coordinates as well as
     *         their Direction from the given Coordinates.
     */
    private static List<Pair<Coordinates, Direction>> getValidAdjacentCoordinatesAndDirectionsForCoordinates(
            Maze maze, Coordinates coordinates) {
        return Arrays.stream(Direction.MOVING_DIRECTIONS)
                .map(d -> createAdjacentCoordinatesAndDirection(coordinates, d))
                .filter(pair -> areCoordinatesValid(maze, pair.getLeft()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new ImmutablePair<Coordinates, Directions> object of the given Direction as well as the adjacent
     * Coordinates from the given Direction of the given Coordinates.
     * @param coordinates Coordinates from which the adjacent Coordinates will be created from
     * @param direction Direction which will be included in the created Pair  and to get the adjacent Coordinates from
     *                  the given Coordinates (which will be then also included in the created Pair object)
     * @return a new ImmutablePair<Coordinates, Directions>  object of the given Direction as well as the adjacent
     *         Coordinates from the given Direction of the given Coordinates.
     */
    private static Pair<Coordinates, Direction> createAdjacentCoordinatesAndDirection(
            Coordinates coordinates, Direction direction) {
        return new ImmutablePair<>(Direction.getNextCoordinates(direction, coordinates), direction);
    }

    /**
     * Checks if the given Coordinates are valid to be checked in the next recursion of the solving algorithm.
     * Coordinates are valid if they are in bounds, have not been already checked and are not Coordinates for a BLOCK
     * Tile in the maze.
     *
     * @param maze Current Maze in the solving algorithm.
     * @param coordinates Coordinates to be checked.
     * @return True if coordinates are valid for further checking, false if not.
     */
    private static boolean areCoordinatesValid(Maze maze, Coordinates coordinates) {
        if (maze.areCoordinatesOutOfBounds(coordinates)) {
            return false;
        }

        boolean notChecked = !maze.areCoordinatesChecked(coordinates);
        boolean walkable = maze.getTileForCoordinates(coordinates) != Tile.BLOCK;

        return notChecked && walkable;
    }

    /**
     * Marks the fastest path found from the entrance to an exit to the given Maze object.
     * The path is marked recursively by going the solution path backwards from the found exit back to the entrance.
     *
     * The solution path is found by checking from which Direction were the current Coordinates arrived from the
     * previous Coordinates, and then going forward opposite of that Direction, repeating until the entrance is found.
     *
     * The solution path is marked in a HashMap<Coordinates, Direction> each solution path Coordinates are a key paired
     * with the Direction POINTING TOWARDS THE NEXT COORDINATES in the solution Path. This is done so that the solution
     * path print will be easier to look at.
     *
     * E.g. if we have a simple maze like this
     *
     * #######
     * #   # E
     * # #   #
     * #^#####
     *
     * If we would print the Directions from which the Coordinates were traversed from the previous tile (i.e. how they
     * are stored during the solving algorithm), the solution print would look like this:
     *
     * #######
     * #↑→→#↑E
     * #↑#↓→→#
     * #^#####
     *
     * But since we use the Directions pointing towards the next Coordinates in the solution path, the result path is
     * much easier to look at (at least in my opinion):
     *
     * #######
     * #→→↓#→E
     * #↑#→→↑#
     * #^#####
     *
     * @param maze Current Maze in the solving algorithm.
     * @param currentCoordinates Current coordinates when traversing through the solution path
     * @param previousDirection Direction pointing towards the next Coordinates in the solution path (NOTE: the param
     *                          is named previousDirection because the method is traversing the solution path backwards
     *                          from the exit back to the entrance).
     */
    private static void markSolution(Maze maze, Coordinates currentCoordinates, Direction previousDirection) {
        if (maze.getTileForCoordinates(currentCoordinates) == Tile.START) {
            return;
        }

        if (previousDirection != null) {
            maze.updateSolutionPath(currentCoordinates, previousDirection);
        }

        Direction currentDirection = maze.getDirectionForCoordinates(currentCoordinates);
        /*
         * NOTE: This method is traversing the solution path backwards, from the exit back to the entrance.
         * Therefore the variables are named "previousDirection" and "nextCoordinates" to reflect the method traversal
         * order (if we were traversing the solution path from the entrance they would be referred to as "nextDirection"
         * and "previousCoordinates" respectively) and not the actual solution path order.
         */
        Coordinates nextCoordinates = Direction.getPreviousCoordinates(currentDirection, currentCoordinates);
        markSolution(maze, nextCoordinates, currentDirection);
    }
}
