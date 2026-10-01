package rysharp.jdk25.addendum.Minor_Api_Additions.code;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ForkJoinPoolExample {

    public static void main(String[] args) throws InterruptedException {

        /*
         * Java 25 makes ForkJoinPool implement
         * ScheduledExecutorService.
         *
         * Therefore this satisfies the normal "is-a" relationship.
         */
        ScheduledExecutorService service =
                new ForkJoinPool();

        /*
         * ForkJoinPool can therefore use scheduling operations.
         *
         * One second is a MINIMUM delay.
         *
         * It does not guarantee execution at exactly 1.000 seconds.
         * After the delay expires, the task becomes eligible to run.
         */
        service.schedule(
                () -> System.out.println("Scheduled task executed"),
                1,
                TimeUnit.SECONDS);

        /*
         * Give the scheduled task enough time to become eligible
         * and execute before shutting the pool down.
         *
         * The sleep is only here to make this demonstration easy
         * to run and observe.
         */
        Thread.sleep(1500);

        service.shutdown();

        /*
         * Java 25 also adds ForkJoinPool.submitWithTimeout().
         *
         * Remember the conceptual distinction:
         *
         * future.get(2, SECONDS)
         *     -> the CALLER waits at most two seconds.
         *
         * pool.submitWithTimeout(...)
         *     -> the TASK itself has an associated timeout policy.
         *
         * submitWithTimeout() is primarily worth recognising for
         * certification purposes.
         */
    }
}