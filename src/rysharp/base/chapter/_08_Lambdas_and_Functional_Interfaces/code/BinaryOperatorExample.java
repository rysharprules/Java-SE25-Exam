package rysharp.base.chapter._08_Lambdas_and_Functional_Interfaces.code;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class BinaryOperatorExample {

    public static void main(String[] args) {
        example1_basicOperators();
        example2_minMaxBy();
        example3_stringLength();
        example4_comparatorDirection();
        example5_reduce();
        example6_reduceWithIdentity();
        example7_methodReferences();
        example8_examTraps();
    }

    // 1. BinaryOperator<T> takes (T, T) and returns T.
    private static void example1_basicOperators() {
        System.out.println("\n1. Basic operators");

        BinaryOperator<Integer> add = (a, b) -> a + b;
        BinaryOperator<Integer> multiply = (a, b) -> a * b;
        BinaryOperator<String> join = (a, b) -> a + "-" + b;

        System.out.println(add.apply(4, 5));          // 9
        System.out.println(multiply.apply(4, 5));     // 20
        System.out.println(join.apply("Java", "25")); // Java-25
    }

    // 2. minBy() and maxBy() are static factory methods.
    private static void example2_minMaxBy() {
        System.out.println("\n2. minBy and maxBy");

        BinaryOperator<Integer> smaller =
                BinaryOperator.minBy(Integer::compare);
        BinaryOperator<Integer> larger =
                BinaryOperator.maxBy(Integer::compare);

        System.out.println(smaller.apply(10, 20)); // 10
        System.out.println(larger.apply(10, 20));  // 20
    }

    // 3. PE3-style question: select the longer String.
    private static void example3_stringLength() {
        System.out.println("\n3. String length comparator");

        Comparator<String> byLength =
                Comparator.comparingInt(String::length);

        BinaryOperator<String> longest =
                BinaryOperator.maxBy(byLength);
        BinaryOperator<String> shortest =
                BinaryOperator.minBy(byLength);

        System.out.println(longest.apply("Java", "Certification"));
        // Certification

        System.out.println(shortest.apply("Java", "Certification"));
        // Java

        // The result is a String, not its length!
        String selected = longest.apply("cat", "elephant");
        System.out.println(selected); // elephant

        // If the comparator reports equality, minBy/maxBy
        // return the FIRST argument.
        System.out.println(longest.apply("cat", "dog")); // cat
    }

    // 4. Reversing a comparator reverses min/max behaviour.
    private static void example4_comparatorDirection() {
        System.out.println("\n4. Reversed comparator");

        Comparator<String> byLength =
                Comparator.comparingInt(String::length);

        Comparator<String> reversed = byLength.reversed();

        BinaryOperator<String> maxReversed =
                BinaryOperator.maxBy(reversed);

        System.out.println(maxReversed.apply("Java", "Python"));
        // Java
    }

    // 5. reduce(BinaryOperator<T>) returns Optional<T>.
    private static void example5_reduce() {
        System.out.println("\n5. reduce without identity");

        List<String> languages = List.of("Java", "Python", "Go");

        BinaryOperator<String> longest =
                BinaryOperator.maxBy(
                        Comparator.comparingInt(String::length)
                );

        Optional<String> result =
                languages.stream().reduce(longest);

        System.out.println(result); // Optional[Python]

        Optional<String> empty =
                List.<String>of().stream().reduce(longest);

        System.out.println(empty); // Optional.empty
    }

    // 6. reduce(identity, operator) returns T, not Optional<T>.
    private static void example6_reduceWithIdentity() {
        System.out.println("\n6. reduce with identity");

        int sum = List.of(1, 2, 3, 4)
                .stream()
                .reduce(0, Integer::sum);

        System.out.println(sum); // 10

        int product = List.of(2, 3, 4)
                .stream()
                .reduce(1, (a, b) -> a * b);

        System.out.println(product); // 24

        int emptySum = List.<Integer>of()
                .stream()
                .reduce(0, Integer::sum);

        System.out.println(emptySum); // 0

        // For parallel reductions, use an associative
        // operator and a genuine identity value.
    }

    // 7. Method references can implement BinaryOperator<T>.
    private static void example7_methodReferences() {
        System.out.println("\n7. Method references");

        BinaryOperator<Integer> sum = Integer::sum;
        BinaryOperator<Integer> maximum = Integer::max;
        BinaryOperator<String> concatenate = String::concat;

        System.out.println(sum.apply(7, 8)); // 15
        System.out.println(maximum.apply(7, 8)); // 8
        System.out.println(concatenate.apply("Java", "25"));
        // Java25
    }

    // 8. Compilation traps and comparison with BiFunction.
    private static void example8_examTraps() {
        System.out.println("\n8. Exam traps");

        BinaryOperator<String> chooseLonger =
                BinaryOperator.maxBy(
                        Comparator.comparingInt(String::length)
                );

        System.out.println(
                chooseLonger.apply("short", "longer")
        ); // longer

        // DOES NOT COMPILE: returns Integer, not String.
        // BinaryOperator<String> wrong =
        //         (a, b) -> a.length() + b.length();

        BiFunction<String, String, Integer> totalLength =
                (a, b) -> a.length() + b.length();

        System.out.println(totalLength.apply("Java", "25"));
        // 6

        // DOES NOT COMPILE: maxBy expects Comparator<T>.
        // BinaryOperator.maxBy(String::length);

        // DOES NOT COMPILE: Comparator argument required.
        // BinaryOperator<String> wrong = BinaryOperator.maxBy();
    }
}
