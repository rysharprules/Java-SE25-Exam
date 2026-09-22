package rysharp.jdk25.addendum.Stream_Gatherers.code;

import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class ScanExample {
    public static void main(String[] args) {
        // scan(Supplier<R> initializer, Gatherer.Integrator<R, T, R> integrator)
        // A stateful gatherer that emits the intermediate results of a fold.
        System.out.println("Scan Example (Running Sum):");
        Stream.of(1, 2, 3, 4)
            .gather(Gatherers.scan(() -> 0, (sum, element) -> sum + element))
            .forEach(System.out::println);
    }
}
