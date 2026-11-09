package rysharp.exam.actionplan.enthuware;

public class ThreadStateQuestions {

    public static void main(String[] args) throws Exception {
        q1NewRunnableTerminated();
        q2TimedWaiting();
        q3Blocked();
        q4Waiting();
    }

    // Q1 - NEW, RUNNABLE, TERMINATED
    static void q1NewRunnableTerminated() throws Exception {
        System.out.println("\nQ1 - Lifecycle");

        Thread t = new Thread(() -> {
            long end = System.nanoTime() + 500_000_000L;

            while (System.nanoTime() < end) {
                // Keep the thread running briefly
            }
        });

        System.out.println("Before start: " + t.getState());

        t.start();
        System.out.println("After start: " + t.getState());

        t.join();
        System.out.println("After join: " + t.getState());
    }

    // Q2 - TIMED_WAITING
    static void q2TimedWaiting() throws Exception {
        System.out.println("\nQ2 - TIMED_WAITING");

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        t.start();
        Thread.sleep(100);

        System.out.println("Worker: " + t.getState());

        t.join();
    }

    // Q3 - BLOCKED
    static void q3Blocked() throws Exception {
        System.out.println("\nQ3 - BLOCKED");

        Object monitor = new Object();

        Thread t = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("Worker acquired monitor");
            }
        });

        synchronized (monitor) {
            t.start();
            Thread.sleep(100);

            System.out.println("Worker: " + t.getState());
        }

        t.join();
    }

    // Q4 - WAITING
    static void q4Waiting() throws Exception {
        System.out.println("\nQ4 - WAITING");

        Object monitor = new Object();

        Thread t = new Thread(() -> {
            synchronized (monitor) {
                try {
                    monitor.wait();//worker releases the monitor and worker enters the WAITING state until it is notified (or interrupted).
                    IO.println("Worker finishing");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        t.start();
        Thread.sleep(100);

        System.out.println("Worker: " + t.getState());

        synchronized (monitor) {
            System.out.println("notifyAll");
            monitor.notifyAll();
        }

        t.join();
    }
}