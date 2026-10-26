package rysharp.exam.retake.kindle;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Kindle 21.4.5 Quiz (p.1371)
public class GatherersQuiz {
    void main() {

        System.out.println("A");
        Stream<Integer> values1 = IntStream.range(1,5).boxed();// 1 to 4
        values1.gather(Gatherers.windowSliding(2))
                .map(vals -> (vals.get(0) + vals.get(1)) / 2.0 + " ")
                .forEach(IO::print);// 1.5 2.5 3.5

        System.out.println("\nB");
        try {
            Random r = new Random();
            Stream<Integer> values2 = IntStream.generate(() -> r.nextInt(100))
                    .boxed().limit(5);// 5 random ints between 0 and 100
            values2.gather(Gatherers.windowFixed(2))
                    // windowsFixed will group into 3 lists. The last list will have one element, thus will throw exception
                    .map(vals -> (vals.get(0) + vals.get(1)) / 2.0 + " ")
                    .forEach(IO::print);
        } catch (ArrayIndexOutOfBoundsException e) { IO.print("ArrayIndexOutOfBoundsException"); }

        System.out.println("\nC");
        Stream<Integer> values3 = IntStream.rangeClosed(1,5).boxed(); // 1 to 5
        values3.gather(Gatherers.fold(
                AtomicInteger::new,
                (sum,i) -> { sum.addAndGet(i); return sum; }))// 1 + 2 + 3 + 4 + 5
                .forEach(IO::print);// 15

        System.out.println("\nD");
        Stream<Integer> values4 = IntStream.rangeClosed(1,5).boxed();
        values4.gather(Gatherers.scan(() -> 0, (sum, i) -> { return sum + i;}))
                .forEach(IO::print);// 1 3 6 10 15
    }
}
