# Maze-Solver

A small Java 11 application for solving mazes parsed from text files.

Must have a Java development kit (JDK) and a Java runtime environment (JRE) installed in your computer (Java version 11 or above)

Instructions:
1. Download the release zip from this repository: https://github.com/eerojala/Maze-Solver/releases/tag/Relase
2. Unzip the folder anywhere you please
3. Create/paste any maze text files in this folder you wish to solve (instructions below for file syntax)
   * The release zip file comes with 2 ready-made maze text files
5. Open a command line and navigate to the unzipped folder
6. Run `java -jar Maze-Solver.jar` in order to start the application
7. When the application is running type the name of the file you wish to solve (or path to the file if it is not located in the same folder root as the jar file)
8. If the maze was able to be parsed/solved then the solution will be printed into the console and also written to a txt file which will be created in the same folder as the jar file
   * NOTE: The solution will contain ASCII arrow characters (↑, → , ↓, ← ) which might not be able to be printed correctly at least in Windows 11. However the solution txt file is encoded in UTF-8 so they should be visible there
9. To exit the program enter `x` or `X`

Maze file syntax:
1. The fle must be a text file e.g. `.txt`, `.md`
2. The file must contain only these characters:
   1. `#` (represents a block which cannot be passed through)
   2. ` ` (i.e. single width whitespace, represents movable space)
   3. `E` (represents an exit)
   4. `^` (represents the starting position, must have exactly one)
 3. The maze must be rectangular, i.e. there must be >0 rows and all of the rows must be the same width
