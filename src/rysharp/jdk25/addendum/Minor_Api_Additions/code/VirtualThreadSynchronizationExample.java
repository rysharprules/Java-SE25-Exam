package rysharp.jdk25.addendum.Minor_Api_Additions.code;

public class VirtualThreadSynchronizationExample {

    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {

        /*
         * Virtual threads are lightweight Java threads scheduled
         * by the JVM onto platform threads known as carrier threads.
         */
        Thread first = Thread.ofVirtual().start(() ->
                synchronizedWork("First"));

        Thread second = Thread.ofVirtual().start(() ->
                synchronizedWork("Second"));

        first.join();
        second.join();

        /*
         * Java 24 changed the implementation of virtual threads so
         * blocking inside normal synchronized code no longer causes
         * the old carrier-thread pinning problem.
         *
         * IMPORTANT:
         *
         * The semantics of synchronized have NOT changed.
         *
         * Only one thread can own LOCK at a time.
         *
         * The improvement is underneath the application:
         * a blocked virtual thread does not inherently need to keep
         * its carrier thread occupied merely because synchronized
         * is involved.
         */
    }

    private static void synchronizedWork(String name) {

        synchronized (LOCK) {

            System.out.println(name + " acquired the lock");

            try {
                /*
                 * Simulate a blocking operation.
                 *
                 * Before Java 24, blocking here while holding a monitor
                 * could pin a virtual thread to its carrier.
                 *
                 * Java 24 removed that normal synchronized pinning issue.
                 */
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(name + " releasing the lock");
        }
    }
}