package rysharp.jdk25.addendum.Stream_Gatherers.code;

import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class FoldExample {
    public static void main(String[] args) {
        // fold(Supplier<R> initializer, Gatherer.Integrator<R, T, R> integrator)
        // A stateful gatherer that folds the elements of the stream into a single result.
        System.out.println("Fold Example (Sum):");
        int sum = Stream.of(1, 2, 3, 4)
            .gather(Gatherers.fold(() -> 0, (sumAcc, element) -> sumAcc + element))
            .findFirst()
            .orElse(0);
        System.out.println("Sum: " + sum);
    }
}
