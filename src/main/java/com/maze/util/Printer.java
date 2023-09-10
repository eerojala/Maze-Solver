package com.maze.util;

public class Printer {
    private Printer() {
        // Empty private constructor for static method class
    }

    /**
     * Prints an empty line to console and starts a new line
     */
    public static void println() {
        println("");
    }

    /**
     * Prints the given String message to console and starts a new line
     * @param msg Message to be printed
     */
    public static void println(String msg) {
        System.out.println(msg);
    }

    /**
     * Prints the given String message to console WITHOUT starting a new line
     * @param msg Message to be printed
     */
    public static void print(String msg) {
        System.out.print(msg);
    }
}
