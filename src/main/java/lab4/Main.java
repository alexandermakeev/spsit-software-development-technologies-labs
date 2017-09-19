package lab4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static lab2.util.Settings.THREADS_COUNT;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public int[][] multiplyMatrices(int[][] a, int[][] b, boolean isParallel) {

        int aRows = a.length;
        if (aRows < 1)
            throw new IllegalArgumentException("Matrix A is empty");
        int bRows = b.length;
        if (bRows < 1)
            throw new IllegalArgumentException("Matrix B is empty");
        int aCols = a[0].length;
        int bCols = b[0].length;

        if (aRows != bCols)
            throw new IllegalArgumentException(String.format("A rows: %d didn't match B cols: %d", aRows, bCols));

        //init
        int[][] res = new int[aRows][bCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                res[i][j] = 0;
            }
        }

        //multiply each A row concurrently
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        for (int i = 0; i < aRows; i++) {
            final int index = i;
            if (isParallel)
                executor.submit(() -> multiply(a, b, res, bRows, aCols, index));
            else
                multiply(a, b, res, bRows, aCols, index);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException("Cannot execute request", e);
        }

        return res;
    }

    private void multiply(int[][] a, int[][] b, int[][] res, int bRows, int aCols, int index) {
        for (int j = 0; j < bRows; j++) {
            for (int k = 0; k < aCols; k++) {
                res[index][j] += a[index][k] * b[k][j];
            }
        }
    }

}
