package rysharp.exam.retake.kindle;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ThreadLocalRandomQuiz {
    void main(){
        // Kindle 21.3.7 Quiz (p.1361)
        q1();
        // Kindle 21.5 Exercise (p.1380)
        q2();
        q3();
    }
    static void q1() {
        /*
        stateful lambdas because sb refers to a mutable object whose state is not managed by the stream pipeline but
        its state is accessed from stream operations. Therefore, when the stream pipeline is parallelized, multiple
        threads employed by the stream pipeline to process the elements of the stream will try to access and mutate sb simultaneously.
         */
        StringBuilder sb = new StringBuilder();
        IntStream is = ThreadLocalRandom.current().ints(0, 10);
        is
                .parallel() // sb will contain exactly 2000 numbers if the parallel operation is removed.
                .takeWhile(i->sb.length()<2001)
                .forEach(s->sb.append(s));
        System.out.println(sb.length()); // The length of sb is unpredictable.
    }

    static void q2() {
            int[] values = ThreadLocalRandom.current()
                    .ints(100)
                    .toArray();

            IntSummaryStatistics stats = Arrays.stream(values)
                    .summaryStatistics();

            // or simply
            IntSummaryStatistics stats2 = ThreadLocalRandom.current()
                .ints(100)
                .summaryStatistics();

            System.out.println(stats.getMin());
            System.out.println(stats.getMax());
            System.out.println(stats.getAverage());
    }

    static void q3() {
        System.out.println("Q4");
        // From a stream of 100000 random integers, filter out the multiples of 5 and print the remaining elements in increasing order.
        // Do the same using sequential as well as parallel streams and compare their performance.
        int[] numbers = ThreadLocalRandom.current()
                .ints(100_000)
                .toArray();

        long start = System.nanoTime();

        int[] result = Arrays.stream(numbers)
                .parallel()
                .filter(n -> n % 5 != 0)
                .sorted()
                .toArray();

        long elapsed = System.nanoTime() - start;
        System.out.println(elapsed); // tends to be slower in parallel
    }
}
