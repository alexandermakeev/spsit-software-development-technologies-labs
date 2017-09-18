package lab3;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static lab2.util.Settings.THREADS_COUNT;

public class Main {

    private final Integer ACCOUNTS_COUNT = 100;
    private final Integer TRANSFERS_COUNT = 10000;
    private final Integer BALANCE_BOUND = 1000;
    private final Random random = new Random();

    private List<Account> accounts;
    private Map<Account, Lock> accountLocks;

    {
        accounts = new ArrayList<>();
        accountLocks = new HashMap<>();
        for (int i = 0; i < ACCOUNTS_COUNT; i++) {
            Account account = new Account(i);
            account.setBalance(random.nextInt(BALANCE_BOUND));
            accounts.add(account);
            accountLocks.put(account, new ReentrantLock());
        }
    }

    private void executeFastButUnsafe() {
        for (int i = 0; i < TRANSFERS_COUNT; i++) {
            Account acc1 = accounts.get(random.nextInt(ACCOUNTS_COUNT));
            Account acc2 = accounts.get(random.nextInt(ACCOUNTS_COUNT));
            Integer amount = random.nextInt(BALANCE_BOUND);
            if (acc1.getBalance() > amount)
                Account.transfer(acc1, acc2, amount);
        }
    }

    private synchronized void executeSafeButSlow() {
        executeFastButUnsafe();
    }

    private void executeWithPossibleDeadlock() {

        for (int i = 0; i < TRANSFERS_COUNT; i++) {
            Account acc1 = accounts.get(random.nextInt(ACCOUNTS_COUNT));
            Account acc2 = accounts.get(random.nextInt(ACCOUNTS_COUNT));
            Lock lock1 = accountLocks.get(acc1);
            Lock lock2 = accountLocks.get(acc2);
            try {
                lock2.lock();
                lock1.lock();

                Integer amount = random.nextInt(BALANCE_BOUND);
                if (acc1.getBalance() > amount)
                    Account.transfer(acc1, acc2, amount);
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }

    }

    private void executeSafeAndFast() {

        for (int i = 0; i < TRANSFERS_COUNT; i++) {
            Account acc1 = accounts.get(random.nextInt(ACCOUNTS_COUNT));
            Account acc2 = accounts.get(random.nextInt(ACCOUNTS_COUNT));
            Lock lock1 = accountLocks.get(acc1);
            Lock lock2 = accountLocks.get(acc2);

            try {
                //acquire locks
                while (true) {
                    boolean gotFirstLock = false;
                    boolean gotSecondLock = false;

                    try {
                        gotFirstLock = lock1.tryLock();
                        gotSecondLock = lock2.tryLock();
                    } finally {
                        if (gotFirstLock && gotSecondLock) //noinspection ContinueOrBreakFromFinallyBlock
                            break;
                        if (gotFirstLock) lock1.unlock();
                        if (gotSecondLock) lock2.unlock();
                    }
                }

                Integer amount = random.nextInt(BALANCE_BOUND);
                if (acc1.getBalance() > amount)
                    Account.transfer(acc1, acc2, amount);

            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        for (int i = 0; i < THREADS_COUNT; i++) {
            executor.submit(main::executeFastButUnsafe);
        }
        executor.shutdown();
    }

}
