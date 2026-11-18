package rysharp.base.chapter._08_Lambdas_and_Functional_Interfaces.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class SupplierExample {

    public static void main(String[] args) {

        // 1. Basic get()
        Supplier<String> greeting = () -> "Hello Java";

        System.out.println("1: " + greeting.get());
        // Hello Java


        // 2. Constructor method reference
        Supplier<List<String>> listFactory = ArrayList::new;

        List<String> first = listFactory.get();
        List<String> second = listFactory.get();

        first.add("Java");

        System.out.println("2: " + first);  // [Java]
        System.out.println("3: " + second); // []


        // 3. A Supplier can return a different value each time
        Random random = new Random();
        //random =  new Random(); // Variable used in lambda expression should be final or effectively final

        Supplier<Integer> dice = () -> random.nextInt(1, 7);

        System.out.println("4: " + dice.get());
        System.out.println("5: " + dice.get());
        // Values between 1 and 6


        // 4. Primitive specialisations
        IntSupplier intSupplier = () -> 25;
        LongSupplier longSupplier = () -> 100L;
        DoubleSupplier doubleSupplier = () -> 3.14;
        BooleanSupplier booleanSupplier = () -> true;

        System.out.println("6: " + intSupplier.getAsInt());         // 25
        System.out.println("7: " + longSupplier.getAsLong());       // 100
        System.out.println("8: " + doubleSupplier.getAsDouble());   // 3.14
        System.out.println("9: " + booleanSupplier.getAsBoolean()); // true


        // 5. Supplier evaluation is deferred until get()
        Supplier<String> lazy = () -> {
            System.out.println("Supplier executed");
            return "Done";
        };

        System.out.println("10: Supplier created");
        System.out.println("11: " + lazy.get());
        // 10: Supplier created
        // Supplier executed
        // 11: Done


        // 6. DOES NOT COMPILE - Supplier has no apply()
        // greeting.apply();

        // 7. DOES NOT COMPILE - BiSupplier does not exist
        // BiSupplier<Integer, Integer> supplier = ...;
    }
}
