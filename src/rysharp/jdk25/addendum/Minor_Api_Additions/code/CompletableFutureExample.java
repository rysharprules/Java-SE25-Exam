package rysharp.jdk25.addendum.Minor_Api_Additions.code;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class CompletableFutureExample {

    public static void main(String[] args) {

        /*
         * Low-priority Java 25 behavioural change:
         *
         * Relevant asynchronous CompletableFuture operations without
         * an explicitly supplied Executor consistently use
         * ForkJoinPool.commonPool().
         */

        CompletableFuture<String> future =
                CompletableFuture
                        /*
                         * supplyAsync() starts asynchronous work that
                         * eventually supplies a value.
                         *
                         * No Executor is supplied here, so the default
                         * common ForkJoinPool is used.
                         */
                        .supplyAsync(() -> "java")

                        /*
                         * thenApplyAsync() asynchronously transforms
                         * the previous result.
                         *
                         * Again, no Executor is supplied.
                         */
                        .thenApplyAsync(String::toUpperCase);

        System.out.println(future.join()); // JAVA

        System.out.println(ForkJoinPool.commonPool());

        /*
         * If an Executor IS explicitly supplied, that Executor is used.
         */
        try (ExecutorService executor =
                     Executors.newSingleThreadExecutor()) {

            CompletableFuture<String> explicit =
                    CompletableFuture.supplyAsync(
                            () -> "Explicit executor",
                            executor);

            System.out.println(explicit.join());
        }

        /*
         * Quick mental model:
         *
         * supplyAsync()
         *     -> asynchronously produce a value
         *
         * thenApplyAsync()
         *     -> asynchronously transform the previous value
         *
         * Async + no supplied Executor
         *     -> common ForkJoinPool
         */
    }
}