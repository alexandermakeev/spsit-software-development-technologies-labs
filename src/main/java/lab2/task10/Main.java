package lab2.task10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static lab2.util.Settings.THREADS_COUNT;

public class Main {

    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    private void thread1() {
        for (int i = 0; i < 1000; i++) {
            lock1.lock();
            lock2.lock();

            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    private void thread2() {
        for (int i = 0; i < 1000; i++) {
            lock2.lock();
            lock1.lock();

            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }


    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        Main main = new Main();
        for (int i = 0; i < THREADS_COUNT / 2; i++) {
            executor.submit(main::thread1);
        }
        for (int i = 0; i < THREADS_COUNT / 2; i++) {

            executor.submit(main::thread2);
        }
        executor.shutdown();
    }
}
