package rysharp.jdk25.addendum.Stream_Gatherers.code;

import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class MapConcurrentExample {
    public static void main(String[] args) {
        // mapConcurrent(int maxConcurrency, Function<? super T, ? extends R> mapper)
        // A stateful gatherer that transforms elements concurrently.
        System.out.println("Map Concurrent Example (Squaring):");
        Stream.of(1, 2, 3, 4)
            .gather(Gatherers.mapConcurrent(2, x -> x * x))
            .forEach(System.out::println);
    }
}
