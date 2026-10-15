package rysharp.base.chapter._10_Streams.code;

import java.util.NoSuchElementException;
import java.util.Optional;

public class OptionalExample {

    public static void main(String[] args) {
        example1_creation();
        example2_getAndChecks();
        example3_orElse();
        example4_orElseGet();
        example5_or();
        example6_orElseThrow();
        example7_ifPresent();
        example8_map();
        example9_flatMap();
        example10_filter();
        example11_stream();
        example12_examTraps();
    }

    // 1. Creating Optional instances
    private static void example1_creation() {
        System.out.println("\n1. Creation");

        Optional<String> a = Optional.of("Java");
        Optional<String> b = Optional.ofNullable(null);
        Optional<String> c = Optional.empty();

        System.out.println(a); // Optional[Java]
        System.out.println(b); // Optional.empty
        System.out.println(c); // Optional.empty

        // Optional.of(null); // NullPointerException
    }

    // 2. Checking and retrieving values
    private static void example2_getAndChecks() {
        System.out.println("\n2. Checks and get()");

        Optional<String> value = Optional.of("Java");

        System.out.println(value.isPresent()); // true
        System.out.println(value.isEmpty());   // false
        System.out.println(value.get());       // Java

        Optional<String> empty = Optional.empty();

        System.out.println(empty.isPresent()); // false
        System.out.println(empty.isEmpty());   // true

        // empty.get(); // NoSuchElementException
    }

    // 3. orElse() evaluates its argument eagerly
    private static void example3_orElse() {
        System.out.println("\n3. orElse()");

        Optional<String> value = Optional.of("Java");

        String result = value.orElse(createDefault());

        System.out.println(result); // Java

        // createDefault() runs even though Optional contains Java.
    }

    // 4. orElseGet() evaluates its Supplier lazily
    private static void example4_orElseGet() {
        System.out.println("\n4. orElseGet()");

        Optional<String> value = Optional.of("Java");

        String result = value.orElseGet(
                OptionalExample::createDefault
        );

        System.out.println(result); // Java

        // createDefault() is NOT called because value is present.
    }

    private static String createDefault() {
        System.out.println("Creating default value");
        return "Default";
    }

    // 5. or() returns another Optional - PE3 Q12
    private static void example5_or() {
        System.out.println("\n5. or()");

        Optional<String> present = Optional.of("Coffee");

        Optional<String> result = present.or(
                () -> Optional.of("Tea")
        );

        System.out.println(result); // Optional[Coffee]

        Optional<String> empty = Optional.empty();

        Optional<String> fallback = empty.or(
                () -> Optional.of("Tea")
        );

        System.out.println(fallback); // Optional[Tea]

        // or() takes Supplier<Optional<T>>
        // and returns Optional<T>.
    }

    // 6. orElseThrow()
    private static void example6_orElseThrow() {
        System.out.println("\n6. orElseThrow()");

        Optional<String> present = Optional.of("Java");

        System.out.println(present.orElseThrow()); // Java

        Optional<String> empty = Optional.empty();

        try {
            empty.orElseThrow();
        } catch (NoSuchElementException e) {
            System.out.println("No value found");
        }

        try {
            empty.orElseThrow(
                    () -> new IllegalArgumentException("Missing value")
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // Missing value
        }
    }

    // 7. ifPresent() and ifPresentOrElse()
    private static void example7_ifPresent() {
        System.out.println("\n7. ifPresent()");

        Optional<String> present = Optional.of("Java");
        Optional<String> empty = Optional.empty();

        present.ifPresent(System.out::println); // Java
        empty.ifPresent(System.out::println);   // Nothing

        present.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Empty")
        ); // Java

        empty.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Empty")
        ); // Empty
    }

    // 8. map() transforms the contained value
    private static void example8_map() {
        System.out.println("\n8. map()");

        Optional<String> text = Optional.of("Java");

        Optional<Integer> length = text.map(String::length);

        System.out.println(length); // Optional[4]

        Optional<String> empty = Optional.empty();

        System.out.println(empty.map(String::length));
        // Optional.empty

        // map() wraps the result in Optional.
        // If the mapper returns null, the result is Optional.empty.
    }

    // 9. flatMap() avoids nested Optionals
    private static void example9_flatMap() {
        System.out.println("\n9. flatMap()");

        Optional<String> text = Optional.of("Java");

        Optional<Optional<String>> nested = text.map(
                s -> Optional.of(s.toUpperCase())
        );

        System.out.println(nested);
        // Optional[Optional[JAVA]]

        Optional<String> flat = text.flatMap(
                s -> Optional.of(s.toUpperCase())
        );

        System.out.println(flat);
        // Optional[JAVA]

        // flatMap() expects a function returning Optional.
    }

    // 10. filter() retains or removes the value
    private static void example10_filter() {
        System.out.println("\n10. filter()");

        Optional<Integer> number = Optional.of(20);

        System.out.println(number.filter(n -> n > 10));
        // Optional[20]

        System.out.println(number.filter(n -> n > 30));
        // Optional.empty
    }

    // 11. stream() converts Optional into a Stream
    private static void example11_stream() {
        System.out.println("\n11. stream()");

        Optional<String> present = Optional.of("Java");
        Optional<String> empty = Optional.empty();

        System.out.println(present.stream().count()); // 1
        System.out.println(empty.stream().count());   // 0

        // Useful for flattening streams of Optionals.
    }

    // 12. Common certification traps
    private static void example12_examTraps() {
        System.out.println("\n12. Exam traps");

        Optional<String> empty = Optional.empty();

        System.out.println(empty.orElse("Fallback"));
        // Fallback (String)

        System.out.println(empty.orElseGet(() -> "Fallback"));
        // Fallback (String)

        System.out.println(empty.or(() -> Optional.of("Fallback")));
        // Optional[Fallback] (Optional<String>)

        // Does not compile:
        // empty.or(() -> "Fallback");
        // Supplier must return Optional<String>.

        // Does not compile:
        // empty.orElseGet("Fallback");
        // orElseGet expects Supplier<String>.

        // Does not compile:
        // empty.ifPresent(s -> s.length()).toString();
        // ifPresent() returns void.

        // Runtime NullPointerException:
        // Optional.of(null);

        // Runtime NoSuchElementException:
        // Optional.empty().get();

        // Runtime NullPointerException:
        // empty.flatMap(s -> null); // Only if value is present!

        Optional<String> present = Optional.of("Java");

        try {
            present.flatMap(s -> null);
        } catch (NullPointerException e) {
            System.out.println("flatMap returned null");
        }
    }
}
