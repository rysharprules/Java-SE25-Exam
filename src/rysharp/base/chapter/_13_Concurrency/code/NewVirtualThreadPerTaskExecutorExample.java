package rysharp.base.chapter._13_Concurrency.code;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class NewVirtualThreadPerTaskExecutorExample {

    public static void main(String[] args)
            throws InterruptedException, ExecutionException {

        example1_SubmissionOrder();
        example2_GetInsideLoop();
        example3_GetAfterSubmission();
        example4_CloseWaitsForTasks();
        example5_SequentialStream();
        example6_ReturnValues();
    }

    // 1. Submission order does NOT guarantee execution order.
    private static void example1_SubmissionOrder() {

        System.out.println("\n1. Submission order");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 1; i <= 5; i++) {
                int task = i;

                executor.submit(() ->
                        System.out.print(task + " "));
            }
        }

        System.out.println("DONE");
        // Possible: 3 1 2 5 4 DONE
        // All five numbers appear before DONE.
    }

    // 2. get() INSIDE the loop forces sequential completion.
    private static void example2_GetInsideLoop()
            throws InterruptedException, ExecutionException {

        System.out.println("\n2. get() inside loop");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 1; i <= 5; i++) {
                int task = i;

                executor.submit(() ->
                        System.out.print(task + " ")).get();
            }
        }

        System.out.println("DONE");
        // Always: 1 2 3 4 5 DONE
    }

    // 3. Submitting ALL tasks before get() does NOT order their output.
    private static void example3_GetAfterSubmission()
            throws InterruptedException, ExecutionException {

        System.out.println("\n3. get() after submission");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<?>> futures = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                int task = i;

                futures.add(executor.submit(() ->
                        System.out.print(task + " ")));
            }

            // Wait for each Future in submission order.
            for (Future<?> future : futures) {
                future.get();
            }
        }

        System.out.println("DONE");
        // Possible: 2 4 1 5 3 DONE
        // get() does not undo concurrent execution.
    }

    // 4. close() waits for all submitted tasks.
    private static void example4_CloseWaitsForTasks() {

        System.out.println("\n4. close() waits");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            executor.submit(() -> {
                sleep(300);
                System.out.print("SLOW ");
            });

            executor.submit(() ->
                    System.out.print("FAST "));

            System.out.print("SUBMITTED ");
        }

        System.out.println("DONE");
        // Typically: SUBMITTED FAST SLOW DONE
        // DONE must be last.
        // SUBMITTED and FAST can appear in either order.
    }

    // 5. Sequential stream submits tasks in encounter order,
    //    but virtual threads can execute in any order.
    private static void example5_SequentialStream() {

        System.out.println("\n5. Sequential stream");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            Stream.of("A", "B", "C")
                    .limit(2)
                    .forEach(s ->
                            executor.submit(() ->
                                    System.out.print(s)));
        }

        System.out.println("DONE");
        // Possible: ABDONE or BADONE
        // C is never submitted.
    }

    // 6. Future.get() also retrieves a Callable's return value.
    private static void example6_ReturnValues()
            throws InterruptedException, ExecutionException {

        System.out.println("\n6. Return values");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<Integer>> futures = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                int task = i;

                futures.add(executor.submit(() -> {
                    sleep((6 - task) * 100L);
                    return task * 10;
                }));
            }

            // Results are retrieved in list order,
            // regardless of which task finished first.
            for (Future<Integer> future : futures) {
                System.out.print(future.get() + " ");
            }
        }

        System.out.println("DONE");
        // Always: 10 20 30 40 50 DONE
    }

    private static void sleep(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
