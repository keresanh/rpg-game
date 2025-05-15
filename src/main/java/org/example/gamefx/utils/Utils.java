package org.example.gamefx.utils;

import javafx.scene.image.Image;

import java.io.*;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of utility methods for common game operations
 */
public class Utils {
    /**
     * Reads world matrix from resource file
     *
     * @param path Resource path to matrix file
     * @param rows Number of rows in matrix
     * @param cols Number of columns per row
     * @return 2D int array representing map tiles
     * @throws IOException If file can't be read
     * @throws IllegalArgumentException For invalid file format
     */
    public static int[][] readWorldMat(String path, int rows, int cols) throws IOException {
        InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + path);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<int[]> rowsList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");
            int[] row = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                row[i] = Integer.parseInt(tokens[i]);
            }
            rowsList.add(row);
        }
        int[][] returnArr = new int[rows][cols];
        for (int i = 0; i < rowsList.size(); i++) {
            for (int j = 0; j < rowsList.get(i).length; j++) {
                returnArr[i][j] = rowsList.get(i)[j];
            }
        }
        return returnArr;
    }

    /**
     * Loads image from application resources
     *
     * @param imgPath Resource path to image file
     * @return Loaded Image object or null on failure
     * @throws IllegalArgumentException If resource not found
     */
    public static Image loadImg(String imgPath) {
        try {
            InputStream is = Utils.class.getResourceAsStream(imgPath);
            if(is == null) {
                throw new IllegalArgumentException("Resource not found: " + imgPath);
            }
            return new Image(is);
        } catch (Exception e) {
            System.err.println("Error loading image: " + imgPath);
            return null;
        }
    }
}
