# Maze-Solver

A small Java 11 application for solving mazes parsed from text files.

Must have Java runtime environment (JRE) installed in your computer (Java version 11 or above) to run the .jar file.
If you want to pull and compile the code yourself then you will also need a Java Development Kit (JDK, Java version 11 or above).

Instructions:
1. Download a release zip from this repository: https://github.com/eerojala/Maze-Solver/releases
3. Unzip the folder anywhere you please
4. Create/paste any maze text files in this folder you wish to solve (instructions below for file syntax)
   * The release zip file comes with 2 ready-made maze text files
5. Open a command line and navigate to the unzipped folder
6. Run `java -jar jarname` in order to start the application, where `jarname` is the name of the jar file you downloaded (e.g. `Maze-Solver-1.0.3.jar`)
8. When the application is running type the name of the file you wish to solve (or path to the file if it is not located in the same folder root as the jar file)
9. If the maze was able to be parsed/solved within 200 steps then the solution will be printed into the console and also written to a txt file which will be created in the same folder as the jar file
10. To exit the program write and enter either `x` or `X`

Maze file syntax:
1. The fle must be a text file e.g. `.txt`, `.md`
2. The file must contain only these characters:
   1. `#` (represents a block which cannot be passed through)
   2. ` ` (i.e. whitespace, represents movable space.)
   3. `E` (represents an exit, multiple are allowed)
   4. `^` (represents the starting position, must have exactly one)
 3. The maze must be rectangular, i.e. there must be >0 rows and all of the rows must be the same width
 4. NOTE: The solving algorithm is currently hard-coded to have a step limit of 200, so the application will be able to parse mazes which require at least >200 steps to solve, but will be unable to actually solve them.
