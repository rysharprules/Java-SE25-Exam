
package rysharp.exam.retake.kindle;

import java.util.concurrent.ThreadLocalRandom;
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
    }
}
