package rysharp.exam.retake.kindle;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

/*
The main distinction is:
- Gatherer.ofSequential() — creates a Gatherer designed for sequential processing, without a custom combiner.
- Gatherer.of() — creates a Gatherer that can support parallel processing when the necessary components, including a combiner, are supplied.
 */
public class GatherersCustomExample {
    void main() {

        // This Gatherer doubles every integer.
        Gatherer<Integer, Void, Integer> doubler =
                Gatherer.ofSequential(
                        // The lambda is the integrator. It receives three arguments:
                        (
                                _, // _ - State (unused in this example)
                         element, // element - Current input element
                        downstream
                        ) // downstream - Where output elements are sent
                                -> {
                            // emits the transformed value and returns a boolean indicating whether downstream wants more elements.
                            return downstream.push(element * 2);
                        }
                );
        // Notice that ofSequential() doesn't necessarily mean the input stream must be sequential.
        // It means the Gatherer itself doesn't provide a combiner for parallel state merging.

        System.out.println(
                Stream.of(1, 2, 3)
                .gather(doubler)
                .map(i -> ""+i)
                .collect(Collectors.joining(","))); // 2,4,6

        /*
         * Custom Gatherer using Gatherer.of():
         * Input:  1, 2, 3
         * Output: 1, 3, 6 (cumulative sums, not individual values)
         */
        Gatherer<Integer, ArrayList<Integer>, Integer> runningSum =
                Gatherer.of(
                        // 1. INITIALIZER - creates an empty ArrayList to hold running totals.
                        ArrayList::new,

                        // 2. INTEGRATOR - processes each element, updates state and pushes a result.
                        (state, element, downstream) -> {
                            int previous = state.isEmpty()
                                    ? 0
                                    : state.getLast();

                            int sum = previous + element;
                            state.add(sum);

                            return downstream.push(sum);
                        },

                        // 3. COMBINER - merges partial states (needed for parallel-capable Gatherers).
                        (left, right) -> {
                            left.addAll(right);
                            return left;
                        },

                        // 4. FINISHER - runs after integration; no action needed here.
                        (_, _) -> {}
                );

        Stream.of(1, 2, 3)
                .gather(runningSum)
                .forEach(System.out::println); // 1 3 6
            }
}
