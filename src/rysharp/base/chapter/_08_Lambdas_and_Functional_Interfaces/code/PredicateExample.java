package rysharp.base.chapter._08_Lambdas_and_Functional_Interfaces.code;

import module java.base;

public class PredicateExample {

    void main() {

        // 1. Basic test()
        Predicate<Integer> positive = n -> n > 0;

        System.out.println("1: " + positive.test(5)); // true


        // 2. and() - both conditions must be true
        Predicate<Integer> even = n -> n % 2 == 0;

        System.out.println("2: " +
                positive.and(even).test(6)); // true

        System.out.println("3: " +
                positive.and(even).test(5)); // false


        // 3. or() - either condition can be true
        System.out.println("4: " +
                positive.or(even).test(-2)); // true


        // 4. negate() - reverse the result
        System.out.println("5: " +
                positive.negate().test(-3)); // true


        // 5. isEqual() - STATIC method
        Predicate<String> isJava = Predicate.isEqual("Java");

        System.out.println("6: " + isJava.test("Java")); // true
        System.out.println("7: " + isJava.test("Kotlin")); // false


        // 6. not() - STATIC method
        Predicate<String> empty = String::isEmpty;

        Predicate<String> notEmpty = Predicate.not(empty);

        System.out.println("8: " + notEmpty.test("Java")); // true
        System.out.println("9: " + notEmpty.test("")); // false


        // 7. Combining multiple predicates
        Predicate<Integer> betweenOneAndTen =
                positive.and(n -> n <= 10);

        System.out.println("10: " +
                betweenOneAndTen.test(7)); // true


        // 8. and() and or() short-circuit
        Predicate<Integer> first = n -> {
            System.out.println("First evaluated");
            return false;
        };

        Predicate<Integer> second = n -> {
            System.out.println("Second evaluated");
            return true;
        };

        System.out.println("11: " +
                first.and(second).test(5));
        // First evaluated
        // 11: false
        // Second is never evaluated

        System.out.println("12: " +
                second.or(first).test(5));
        // Second evaluated
        // 12: true
        // First is never evaluated

        // 9. DOES NOT COMPILE - no apply() or get()
        // positive.apply(5);
        // positive.get();
    }
}
