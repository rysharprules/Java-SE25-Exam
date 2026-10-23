import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
                .filter(n -> n < 10_000)
                .limit(10_000)
                .count();
        System.out.println("Filtered then limited count = " + count);
        // An unbounded takeWhile(n -> n < 10_000) never terminates here.
    }
}
