
package rysharp.exam.actionplan.enthuware;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerQuestions {

    public static void main(String[] args) throws InterruptedException {
        Q1();
        Q2();
        Q3();
        Q4();
        Q5();
    }

    static void Q1() {
        System.out.println("\nQ1 - Increment methods");

        AtomicInteger ai = new AtomicInteger(10);

        System.out.println(ai.incrementAndGet()); // 11
        System.out.println(ai.getAndIncrement()); // 11
        System.out.println(ai.get());             // 12

        System.out.println(ai.decrementAndGet()); // 11
        System.out.println(ai.getAndDecrement()); // 11
        System.out.println(ai.get());             // 10
    }

    static void Q2() {
        System.out.println("\nQ2 - Add methods");

        AtomicInteger ai = new AtomicInteger(10);

        System.out.println(ai.addAndGet(5)); // 15
        System.out.println(ai.getAndAdd(5)); // 15
        System.out.println(ai.get());        // 20

        System.out.println(ai.addAndGet(-3)); // 17

        // DOES NOT COMPILE:
        // ai.increment();
        // ai.add(1);
        // ai++;
    }

    static void Q3() {
        System.out.println("\nQ3 - set(), getAndSet(), compareAndSet()");

        AtomicInteger ai = new AtomicInteger(10);

        ai.set(20);
        System.out.println(ai.get()); // 20

        System.out.println(ai.getAndSet(30)); // 20
        System.out.println(ai.get());         // 30

        // compareAndSet(expected, update)
        System.out.println(ai.compareAndSet(30, 40)); // true
        System.out.println(ai.get());                 // 40

        System.out.println(ai.compareAndSet(30, 50)); // false
        System.out.println(ai.get());                 // 40
    }

    static void Q4() {
        System.out.println("\nQ4 - Atomic vs non-atomic compound action");

        AtomicInteger ai = new AtomicInteger(10);

        // Each method call is atomic, but the combination
        // of get() followed by set() is NOT atomic.
        ai.set(ai.get() + 1);

        System.out.println(ai.get()); // 11

        // Correct atomic alternatives:
        ai.incrementAndGet();
        ai.getAndIncrement();
        ai.addAndGet(1);
        ai.getAndAdd(1);

        System.out.println(ai.get()); // 15
    }

    static void Q5() throws InterruptedException {
        System.out.println("\nQ5 - Multiple threads");

        AtomicInteger counter = new AtomicInteger();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 1_000; i++) {
            executor.submit(counter::incrementAndGet);
        }

        executor.shutdown();

        boolean finished = executor.awaitTermination(
                10, TimeUnit.SECONDS);

        System.out.println("Finished: " + finished);
        System.out.println("Counter: " + counter.get());

        // If all tasks complete:
        // Finished: true
        // Counter: 1000

        // Contrast with an ordinary shared int increment:
        // counter++ is not atomic.
    }
}
