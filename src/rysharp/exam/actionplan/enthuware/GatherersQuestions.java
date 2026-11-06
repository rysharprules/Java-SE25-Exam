package rysharp.exam.actionplan.enthuware;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class GatherersQuestions {
    void main() {
        q1();
        q2();
    }

    static void q1() {
        String[] codes = {"a", "ab", "aaa"};
        Arrays.stream(codes)
                .gather(Gatherers.windowSliding(2))
                //List<String> of size 2 [a, ab]
                //List<String> of size 2 [ab, aaa]
                .gather(Gatherers.scan(() -> 0, (a, b) -> a + b.size()))
                // () -> 0 initializes the accumulator once.
                //a is the current accumulated integer.
                //b is the current window (List<String>).
                //b.size() is 2 for both windows.
                .forEach(System.out::println);// 2 4
        // scan() emits every updated accumulator value, whereas fold() emits only the final accumulated result.
        // So if you changed scan to fold:
        // .gather(Gatherers.fold(() -> 0, (a, b) -> a + b.size()))
        // the output would simply be: 4
    }

    static void q2() {
        System.out.println("\nQ2");
        // Given:
//        String[] codes = {"a", "ab", "aaa"};
//        return Arrays.stream(codes);
        // Which of the following lines when inserted in the above code independent of each other will print exactly 3 as the output?
        // (Ignoring new or empty lines).

        //a) CORRECT ANSWER
        codes.get().gather(Gatherers.windowFixed(2))
                .gather(Gatherers.fold(
                        () -> 0,
                        (a, b) -> a + b.size()
                ))
                .forEach(System.out::println);
        // prints: 3
        // Gatherers.windowFixed(2) will produce a stream containing two lists - [a, ab], [aaa].
        // Gatherers.fold(()->0, (a, b)->a+b.size() will produce a stream containing 3.

        //b)
        System.out.println(codes.get().map(str -> str.length()).max((a, b) -> a - b));
        // prints: Optional[3]
        // max() returns an Optional.

        //c)
        System.out.println(codes.get().gather(Gatherers.scan(String::new, (a, b) -> a + b)).distinct());
        // prints: java.util.stream.DistinctOps$1@776ec8df
        // distinct() is an intermediate operation, and it produces a Stream

        //d)
        System.out.println(codes.get().gather(Gatherers.windowSliding(3)).count());
        // prints: 1
        // Gatherers.windowSliding(3) will group the incoming elements into a list containing 3 elements each
        // Since there are only 3 elements in the incoming stream, Gatherers.windowSliding(3) will generate only one element,
        //      which will be a List containing the three elements of the original stream, in the output stream.

        //e)
        System.out.println(codes.get()
                .gather(Gatherers.windowSliding(1))
                .gather(Gatherers.fold(() -> 0, (a, b) -> a + b.size()))
                .findFirst());
        // prints: Optional[3]
        // Gatherers.windowSliding(1) will produce a stream containing three lists - [a], [ab], [aaa].
        // Gatherers.fold(()->0, (a, b)->a+b.size() will produce a stream containing 3.
        // findFirst() will produce an Optional containing 3.
    }

    static Supplier<Stream<String>> codes = () -> {
        String[] codes = {"a", "ab", "aaa"};
        return Arrays.stream(codes);
    };
}

