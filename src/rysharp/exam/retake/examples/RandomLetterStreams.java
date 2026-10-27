package rysharp.exam.retake.examples;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

// Q21 POST_EXAM_REPORT.md
public class RandomLetterStreams {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom.current().ints('A', 'Z' + 1)
                .limit(10_000)
                .forEach(n -> builder.append((char) n));
        System.out.println("StringBuilder length = " + builder.length());

        String result = ThreadLocalRandom.current()
                .ints(10_000, 'A', 'Z' + 1)
                .mapToObj(n -> String.valueOf((char) n))
                .collect(Collectors.joining());
        System.out.println("Collected length = " + result.length());

        long count = ThreadLocalRandom.current().ints('A', 'Z' + 1)
//                .peek(System.out::println)
                .takeWhile(n -> n < 10_000) // no impact
                .filter(n -> n < 10_000) // no impact
                .limit(10_000) // required to avoid infinite stream
                .count();
        System.out.println("Filtered then limited count = " + count);
    }
}
