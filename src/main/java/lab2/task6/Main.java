package lab2.task6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static lab2.util.Settings.THREADS_COUNT;

public class Main {

    private int runs = 0;
//    private volatile int runs = 0; //synchronized only read
//    private AtomicInteger runs = new AtomicInteger(0);

    private void countUp() {
        while (runs <= 10) {
            int temp = runs;
            System.out.println(
                    String.format("%s %d",
                            Thread.currentThread().getName(),
                            ++temp)
            );
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runs = temp;
        }
    }

    private void runThreads() {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        for (int i = 0; i < THREADS_COUNT; i++) {
            executor.submit(this::countUp);
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        new Main().runThreads();
    }
}
