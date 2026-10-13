package rysharp.base.chapter._08_Lambdas_and_Functional_Interfaces.code;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BiFunctionExample {

    public static void main(String[] args) {

        // 1. Basic apply() - two inputs, one result
        BiFunction<Integer, Integer, Integer> add =
                (a, b) -> a + b;

        System.out.println("1: " + add.apply(5, 3)); // 8


        // 2. Different input and return types
        BiFunction<String, String, Integer> totalLength =
                (a, b) -> a.length() + b.length();

        System.out.println("2: " + totalLength.apply("Java", "25")); // 6


        // 3. andThen() - transform the result
        Function<Integer, Integer> doubleIt = n -> n * 2;

        System.out.println("3: " +
                add.andThen(doubleIt).apply(5, 3)); // 16


        // 4. andThen() can change the return type
        Function<Integer, String> describe =
                n -> "Result: " + n;

        BiFunction<Integer, Integer, String> formatted =
                add.andThen(describe);

        System.out.println("4: " + formatted.apply(5, 3));
        // Result: 8


        // 5. Method reference
        BiFunction<Integer, Integer, Integer> max =
                Integer::max;

        System.out.println("5: " + max.apply(10, 20)); // 20


        // 6. Chaining andThen()
        System.out.println("6: " +
                add.andThen(doubleIt)
                        .andThen(describe)
                        .apply(5, 3));
        // Result: 16


        // 7. DOES NOT COMPILE - BiFunction has no compose()
        // add.compose(doubleIt);
    }
}
