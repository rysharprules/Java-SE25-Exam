package rysharp.base.chapter._08_Lambdas_and_Functional_Interfaces.code;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public class ConsumerExample {

    public static void main(String[] args) {

        // 1. Consumer<T>
        // Abstract method: void accept(T)
        // Consumes a value and returns nothing.

        Consumer<String> printer = s -> System.out.println(s);
        printer.accept("Hello Consumer");

        // Method reference
        Consumer<String> printReference = System.out::println;
        printReference.accept("Method reference");


        // 2. andThen()
        // Executes the first Consumer, then the second.
        // Returns a new Consumer.

        Consumer<String> first = s -> System.out.println("First: " + s);
        Consumer<String> second = s -> System.out.println("Second: " + s);

        Consumer<String> combined = first.andThen(second);
        combined.accept("Java");

        // Output:
        // First: Java
        // Second: Java


        // 3. BiConsumer<T, U>
        // Abstract method: void accept(T, U)
        // Consumes two values and returns nothing.

        BiConsumer<String, Integer> person =
                (name, age) -> System.out.println(name + ": " + age);

        person.accept("Alice", 25);

        // BiConsumer also supports andThen().

        BiConsumer<String, Integer> greeting =
                (name, _) -> System.out.println("Hello " + name);

        person.andThen(greeting).accept("Bob", 30);


        // 4. Primitive Consumer specialisations
        // Avoid boxing primitive values.

        IntConsumer intConsumer = n -> System.out.println(n * 2);
        intConsumer.accept(10); // 20

        LongConsumer longConsumer = n -> System.out.println(n + 1);
        longConsumer.accept(100L); // 101

        DoubleConsumer doubleConsumer = n -> System.out.println(n / 2);
        doubleConsumer.accept(9.0); // 4.5

        // Primitive Consumers also support andThen().
        IntConsumer addOne = n -> System.out.println(n + 1);
        intConsumer.andThen(addOne).accept(5);

        // Output:
        // 10
        // 6


        // 5. Consumer vs Function
        // Consumer returns void.
        // Function returns a result.

        Consumer<String> consumer = s -> System.out.println(s.length());
        consumer.accept("Java"); // Prints 4

        // INVALID: Consumer.accept() returns void.
        // int length = consumer.accept("Java");


        // 6. Common compilation traps

        // INVALID: Consumer has no compose() method.
        // first.compose(second);

        // INVALID: Consumer takes exactly one argument.
        // Consumer<String, Integer> invalid;

        // INVALID: accept() returns void.
        // String result = printer.accept("Hello");

        // INVALID: Consumer has no test() method.
        // printer.test("Hello");

        // INVALID: BiConsumer has no compose() method.
        // person.compose(greeting);
    }
}
