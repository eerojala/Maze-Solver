package com.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                System.out.print("Enter file name: ");

                try {
                    String filePath = reader.readLine();
                    var maze = MazeParser.parseMaze(filePath);
                    printMaze(maze);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } finally {
            // IOException is thrown outside of main
            reader.close();
        }
    }

    private static void printMaze(Tile[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j].getSymbol());
            }
            System.out.println();
        }
    }
}
