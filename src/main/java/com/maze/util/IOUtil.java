package com.maze.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {
    private IOUtil() {
        // Empty private constructor for static method class
    }

    /**
     * Attempts to close the given Closeable -object if it is not null.
     *
     * @param closeable object of a class which implements Closeable, e.g. PrintWriter or BufferedReader
     * @throws IOException if closing was unsuccessful
     */
    public static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }
}
