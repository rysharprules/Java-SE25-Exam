
package rysharp.exam.actionplan.kindle;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class ThreadLocalRandomExample {

    void main() {

        // 1. Unlimited stream of random integers (full int range).
        IntStream is1 = ThreadLocalRandom.current().ints();

        // An infinite stream needs a limiting operation before
        // a terminal operation such as toList(), count(), or sum().
        System.out.println("Any 5 ints: "
                + is1.limit(5).boxed().toList());


        // 2. Unlimited stream within a specified range.
        // Lower bound inclusive, upper bound exclusive: [10, 1000)
        IntStream is2 = ThreadLocalRandom.current().ints(10, 1000);

        System.out.println("5 ints between 10 and 999: "
                + is2.limit(5).boxed().toList());


        // 3. Finite stream containing exactly 100 random integers.
        IntStream is3 = ThreadLocalRandom.current().ints(100);

        System.out.println("Count: " + is3.count()); // 100


        // 4. Finite stream of 10 integers within [1, 7).
        // This simulates rolling a six-sided die 10 times.
        IntStream is4 = ThreadLocalRandom.current().ints(10, 1, 7);

        System.out.println("Dice rolls: "
                + is4.boxed().toList());


        // 5. Random uppercase letters: 'A' to 'Z' inclusive.
        // nextInt/ints upper bounds are EXCLUSIVE.
        String letters = ThreadLocalRandom.current()
                .ints(10, 'A', 'Z' + 1)
                .collect(
                        StringBuilder::new,
                        (sb, c) -> sb.append((char) c),
                        StringBuilder::append
                )
                .toString();

        System.out.println("Letters: " + letters);


        // 6. Parallel random number generation.
        // ThreadLocalRandom.current() is called by each worker.
        long total = IntStream.range(0, 1000)
                .parallel()
                .map(i -> ThreadLocalRandom.current().nextInt(1, 7))
                .asLongStream()
                .sum();

        System.out.println("Total of 1000 dice rolls: " + total);


        // 7. takeWhile() vs filter() on a random stream.
        // filter keeps searching for matching values.
        long filtered = ThreadLocalRandom.current()
                .ints(100, 0, 10)
                .filter(i -> i < 5)
                .count();

        // takeWhile stops at the FIRST value that fails.
        long taken = ThreadLocalRandom.current()
                .ints(100, 0, 10)
                .takeWhile(i -> i < 5)
                .count();

        System.out.println("Filtered count: " + filtered);
        System.out.println("TakeWhile count: " + taken);


        // 8. ME12 Q37 - Random double streams.
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Finite stream: exactly 10 random doubles.
        DoubleStream ds1 = random.doubles(10);

        System.out.println("10 doubles: "
                + ds1.boxed().toList());


        // Unlimited stream, limited to 10 elements.
        DoubleStream ds2 = random.doubles();

        System.out.println("10 limited doubles: "
                + ds2.limit(10).boxed().toList());


        // DoubleStream.generate() accepts a DoubleSupplier.
        // nextDouble() supplies each value.
        DoubleStream ds3 = DoubleStream.generate(random::nextDouble);

        System.out.println("10 generated doubles: "
                + ds3.limit(10).boxed().toList());


        // Random has the same doubles() methods.
        Random r = new Random();

        System.out.println("Random doubles: " // and ints and longs
                + r.doubles(10).boxed().toList());// DoubleStream

        System.out.println(r.nextBoolean());
        System.out.println(r.nextDouble()); // [0.0, 1.0)
        System.out.println(r.nextFloat());  // [0.0, 1.0)
        System.out.println(r.nextLong());

        // 10. Single values vs streams

        int a = r.nextInt();        // Any int
        int b = r.nextInt(10);      // 0 to 9
        int c = r.nextInt(5, 10);   // 5 to 9

        IntStream stream = r.ints(5, 10, 20);
// Exactly 5 integers, each from 10 to 19
    }
}
