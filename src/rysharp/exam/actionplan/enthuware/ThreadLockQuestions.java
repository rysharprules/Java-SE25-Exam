package rysharp.exam.actionplan.enthuware;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadLockQuestions {

    public static void main(String[] args) throws Exception {

        // Q1 - Reentrant locking and hold counts
        System.out.println("Q1");
        ReentrantLock lock = new ReentrantLock();

        lock.lock();
        try {
            System.out.println("First lock: " + lock.getHoldCount());

            lock.lock();
            try {
                System.out.println("Second lock: " + lock.getHoldCount());
                System.out.println("Locked: " + lock.isLocked());
                System.out.println("Held by me: "
                        + lock.isHeldByCurrentThread());
            } finally {
                lock.unlock();
            }

            System.out.println("After inner unlock: "
                    + lock.getHoldCount());

        } finally {
            lock.unlock();
        }

        System.out.println("After both unlocks: "
                + lock.getHoldCount());


        // Q2 - tryLock() when the lock is available
        System.out.println("\nQ2");
        ReentrantLock lock2 = new ReentrantLock();

        if (lock2.tryLock()) {
            try {
                System.out.println("Acquired: " + lock2.isLocked());
                System.out.println("Hold count: " + lock2.getHoldCount());
            } finally {
                lock2.unlock();
            }
        }


        // Q3 - tryLock() when another thread owns the lock
        System.out.println("\nQ3");
        ReentrantLock lock3 = new ReentrantLock();

        lock3.lock();

        Thread worker = Thread.ofPlatform().start(() -> {
            boolean acquired = lock3.tryLock();

            System.out.println("Worker acquired: " + acquired);

            if (acquired) {
                lock3.unlock();
            }
        });

        worker.join();
        lock3.unlock();


        // Q4 - Missing unlock: the ME11 Q20 trap
        System.out.println("\nQ4");
        ReentrantLock lock4 = new ReentrantLock();

        if (lock4.tryLock()) {
            lock4.lock();

            System.out.println("Hold count after two acquisitions: "
                    + lock4.getHoldCount());

            lock4.unlock();

            System.out.println("Hold count after one unlock: "
                    + lock4.getHoldCount());

            // We still own the lock!
            System.out.println("Still locked: " + lock4.isLocked());

            // Clean up the remaining acquisition
            lock4.unlock();
        }


        // Q5 - lock() versus tryLock()
        System.out.println("\nQ5");
        ReentrantLock lock5 = new ReentrantLock();

        lock5.lock();

        Thread contender = Thread.ofPlatform().start(() -> {
            System.out.println("Contender attempting lock()");

            lock5.lock(); // Waits until main releases it
            try {
                System.out.println("Contender acquired lock()");
            } finally {
                lock5.unlock();
            }
        });

        Thread.sleep(100);
        System.out.println("Main releasing lock");
        lock5.unlock();

        contender.join();
    }
}