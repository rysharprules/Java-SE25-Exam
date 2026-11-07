
package rysharp.exam.actionplan.enthuware;

import java.util.*;
import java.util.stream.*;

public class StreamsPrimitiveCollectors {

    public static void main(String[] args) {
        Q1();
        Q2();
        Q3();
        Q4();
        Q5();
    }

    static void Q1() {
        // IntStream does NOT accept a Collector.
        IntStream nums = IntStream.range(1, 5);

        // DOES NOT COMPILE:
        // double avg = nums.collect(Collectors.averagingInt(i -> i));

        // Primitive streams have their own average() terminal operation.
        double avg = nums.average().getAsDouble();

        System.out.println(avg); // 2.5
    }

    static void Q2() {
        // Convert IntStream to Stream<Integer> using boxed().
        // Stream<T> supports collect(Collector).
        double avg = IntStream.range(1, 5)
                .boxed()
                .collect(Collectors.averagingInt(i -> i));

        System.out.println(avg); // 2.5

        // mapToObj() is another way to create an object stream.
        double avg2 = IntStream.range(1, 5)
                .mapToObj(i -> i)
                .collect(Collectors.averagingInt(i -> i));

        System.out.println(avg2); // 2.5
    }

    static void Q3() {
        // Primitive stream collect() takes THREE arguments:
        // supplier, accumulator, combiner.
        ArrayList<Integer> result = IntStream.range(1, 5)
                .collect(
                        ArrayList::new,// Supplier
                        ArrayList::add,// ObjIntCosumer
                        ArrayList::addAll// BiConsumer
                );

        System.out.println(result); // [1, 2, 3, 4]

        // Note: this collect() is NOT using a Collector object.
    }

    static void Q4() {
        // IntStream.summaryStatistics() returns IntSummaryStatistics.
        IntSummaryStatistics stats = IntStream.range(1, 5)
                .summaryStatistics();

        System.out.println(stats.getCount());   // 4
        System.out.println(stats.getSum());     // 10
        System.out.println(stats.getMin());     // 1
        System.out.println(stats.getMax());     // 4
        System.out.println(stats.getAverage()); // 2.5

        // Collectors.summarizingInt() is for Stream<T>, NOT IntStream.
        IntSummaryStatistics stats2 = Stream.of("A", "BB", "CCC")
                .collect(Collectors.summarizingInt(String::length));

        System.out.println(stats2.getSum()); // 6
    }

    static void Q5() {
        // Primitive streams use parallel(), NOT parallelStream().
        double avg = IntStream.range(1, 5)
                .parallel()
                .mapToDouble(i -> i)
                .average()
                .getAsDouble();

        System.out.println(avg); // 2.5

        // DOES NOT COMPILE:
        // IntStream.range(1, 5).parallelStream();

        // DOES NOT COMPILE: already an IntStream.
        // IntStream.range(1, 5).mapToInt(i -> i);

        // Collection has parallelStream().
        List<Integer> list = List.of(1, 2, 3, 4);
        System.out.println(
                list.parallelStream().mapToInt(i -> i).sum()
        ); // 10
    }
}
