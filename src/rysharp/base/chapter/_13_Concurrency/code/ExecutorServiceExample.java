package rysharp.base.chapter._13_Concurrency.code;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceExample {

    public static void main(String[] args) throws Exception {

        example1_Shutdown();
        example2_ShutdownNow();
        example3_AwaitTermination();
        example4_InvokeAll();
        example5_Interrupt();
        example6_ExecuteVsSubmit();
    }

    // 1. shutdown()
    // Rejects new tasks but allows submitted tasks to finish.
    // Does NOT wait for termination.

    private static void example1_Shutdown() throws Exception {
        System.out.println("\n--- shutdown() ---");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> System.out.println("Task 1"));
        executor.submit(() -> System.out.println("Task 2"));

        executor.shutdown();

        System.out.println("Shutdown requested");

        // New submissions are rejected after shutdown().
        try {
            executor.submit(() -> System.out.println("Task 3"));
        } catch (RejectedExecutionException e) {
            System.out.println("Task 3 rejected");
        }

        // Wait for previously submitted tasks to finish.
        boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Terminated: " + terminated);
    }

    // 2. shutdownNow()
    // Attempts to interrupt running tasks.
    // Returns tasks that never started.
    // Does NOT guarantee immediate termination.

    private static void example2_ShutdownNow() throws Exception {
        System.out.println("\n--- shutdownNow() ---");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        CountDownLatch started = new CountDownLatch(1);

        executor.submit(() -> {
            System.out.println("Task 1 started");
            started.countDown();

            try {
                Thread.sleep(10_000);
                System.out.println("Task 1 finished normally");
            } catch (InterruptedException e) {
                System.out.println("Task 1 interrupted");
                Thread.currentThread().interrupt();
            }
        });

        // Queued behind Task 1 because there is only one worker.
        executor.submit(() -> System.out.println("Task 2"));

        started.await();

        List<Runnable> notStarted = executor.shutdownNow();

        System.out.println("Tasks not started: " + notStarted.size());

        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    // 3. awaitTermination()
    // Waits for termination AFTER shutdown is requested.
    // Returns true if terminated, false if timeout expires.

    private static void example3_AwaitTermination() throws Exception {
        System.out.println("\n--- awaitTermination() ---");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                Thread.sleep(200);
                System.out.println("Work complete");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();

        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        System.out.println("Terminated: " + terminated);
    }

    // 4. invokeAll()
    // Submits a collection of Callable tasks.
    // Waits until ALL tasks complete.
    // Returns List<Future<T>> in INPUT ORDER.
    // Individual task failures are reported by Future.get().

    private static void example4_InvokeAll() throws Exception {
        System.out.println("\n--- invokeAll() ---");

        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {

            List<Callable<String>> tasks = List.of(
                    () -> {
                        Thread.sleep(300);
                        return "Task A";
                    },
                    () -> {
                        Thread.sleep(100);
                        return "Task B";
                    },
                    () -> {
                        Thread.sleep(200);
                        return "Task C";
                    }
            );

            List<Future<String>> results = executor.invokeAll(tasks);

            // Always A, B, C because the Futures retain input order.
            for (Future<String> future : results) {
                System.out.println(future.get());
            }
        }
    }

    // 5. interrupt()
    // Interruption is a REQUEST, not a forced stop.
    // sleep(), wait(), join() respond by throwing
    // InterruptedException and clearing interrupt status.

    private static void example5_Interrupt() throws Exception {
        System.out.println("\n--- interrupt() ---");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        CountDownLatch started = new CountDownLatch(1);

        Future<?> future = executor.submit(() -> {
            started.countDown();

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Working...");
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupted");

                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }

            System.out.println("Task exiting");
        });

        started.await();

        // Allow the task to do some work.
        Thread.sleep(500);

        // Requests interruption of the running task.
        future.cancel(true);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Future cancelled: " + future.isCancelled());

        // future.get() would throw CancellationException.
    }

    // 6. execute() vs submit()
    // execute(Runnable) returns void.
    // submit(Runnable) returns Future<?>.
    // submit(Callable<T>) returns Future<T>.

    private static void example6_ExecuteVsSubmit() throws Exception {
        System.out.println("\n--- execute() vs submit() ---");

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {

            executor.execute(() ->
                    System.out.println("execute(): no Future"));

            Future<?> runnableFuture = executor.submit(() ->
                    System.out.println("submit(Runnable)"));

            Future<Integer> callableFuture = executor.submit(() -> 42);

            System.out.println("Runnable result: " + runnableFuture.get());
            System.out.println("Callable result: " + callableFuture.get());
        }
    }
}
