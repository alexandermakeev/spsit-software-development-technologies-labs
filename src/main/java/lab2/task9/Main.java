package lab2.task9;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static lab2.util.Settings.THREADS_COUNT;

public class Main {

    private int runs = 0;
    private final Semaphore semaphore = new Semaphore(1);

    private void countUp() {
        while (runs <= 10) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int temp = runs;
            temp++;
            System.out.println(
                    String.format("%s %d",
                            Thread.currentThread().getName(), temp)
            );
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runs = temp;
            semaphore.release();
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