package rysharp.base.chapter._08_Lambdas_and_Functional_Interfaces.code;

import java.util.function.Function;

public class FunctionExample {

    public static void main(String[] args) {

        // 1. Basic Function<T, R>
        Function<String, Integer> length = String::length;

        System.out.println("1: " + length.apply("Java")); // 4


        // 2. andThen() - execute THIS function first, THEN the argument
        Function<Integer, Integer> addThree = n -> n + 3;
        Function<Integer, Integer> doubleIt = n -> n * 2;

        System.out.println("2: " + addThree.andThen(doubleIt).apply(5)); // 16
        // (5 + 3) * 2 = 16


        // 3. compose() - execute the argument BEFORE this function
        System.out.println("3: " + addThree.compose(doubleIt).apply(5)); // 13
        // (5 * 2) + 3 = 13


        // 4. Chaining multiple functions
        Function<Integer, Integer> subtractOne = n -> n - 1;

        Function<Integer, Integer> chain = addThree
                .andThen(doubleIt)
                .andThen(subtractOne);

        System.out.println("4: " + chain.apply(5)); // 15
        // ((5 + 3) * 2) - 1 = 15


        // 5. Function.identity() - returns its input unchanged
        Function<String, String> identity = Function.identity();

        System.out.println("5: " + identity.apply("Java")); // Java


        // 6. Different input and output types
        Function<String, Integer> parse = Integer::parseInt;
        Function<Integer, String> describe = n -> "Value: " + n;

        System.out.println("6: " + parse.andThen(describe).apply("25"));
        // Value: 25


        // 7. compose() with different types
        Function<String, Integer> textLength = String::length;
        Function<Integer, String> toText = n -> "Number " + n;

        System.out.println("7: " + textLength.compose(toText).apply(123));
        // "Number 123" has 10 characters -> 10


        // 8. BiFunction does NOT have compose()
        // BiFunction<Integer, Integer, Integer> sum = Integer::sum;
        // sum.compose(...); // DOES NOT COMPILE
        //
        // BiFunction supports andThen(), but not compose().
    }
}
