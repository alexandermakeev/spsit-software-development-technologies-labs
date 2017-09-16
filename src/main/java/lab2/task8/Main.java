package lab2.task8;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static lab2.util.Settings.THREADS_COUNT;

public class Main {

    private int runs = 0;
    private final Lock lock = new ReentrantLock();

    private void countUp() {
        while (runs <= 10) {
            lock.lock();
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
            lock.unlock();
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
