package rysharp.jdk25.addendum.Stream_Gatherers.code;

import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class WindowExample {
    public static void main(String[] args) {
        // windowFixed(int size)
        // Groups stream elements into lists of a fixed size. Useful for batch processing.
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        System.out.println("Window Fixed Example:");
        list.stream()
            .gather(Gatherers.windowFixed(2))
            .forEach(System.out::println);

        // windowSliding(int size)
        // Similar to windowFixed, but windows overlap.
        System.out.println("\nWindow Sliding Example:");
        list.stream()
            .gather(Gatherers.windowSliding(2))
            .forEach(System.out::println);
    }
}
