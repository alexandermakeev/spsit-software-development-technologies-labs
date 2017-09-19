package lab4;

import org.junit.Test;

import java.util.Random;

public class MainTest {

    private final int[][] a;
    private final int[][] b;
    private final Main main;

    {
        main = new Main();
        Random random = new Random();
        int M = 2000;
        int N = 1000;
        a = new int[M][N];
        b = new int[N][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                a[i][j] = random.nextInt(M);
                b[j][i] = random.nextInt(N);
            }
        }
    }

    @Test
    public void multiply() {
        main.multiplyMatrices(a, b, false);
    }

    @Test
    public void multiplyParallel() {
        main.multiplyMatrices(a, b, true);
    }

}