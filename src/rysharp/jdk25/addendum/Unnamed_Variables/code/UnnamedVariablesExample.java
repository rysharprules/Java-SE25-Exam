package rysharp.jdk25.addendum.Unnamed_Variables.code;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class UnnamedVariablesExample {
    // A sample record for pattern matching examples
    record Point(int x, int y) {}

    public static void main(String[] args) {
        System.out.println("--- Unnamed Variables (_) Examples ---");

        // 1. Catch blocks
        try {
            Integer.parseInt("not-a-number");
        } catch (NumberFormatException _) {
            System.out.println("Caught exception, ignored its name.");
        }

        // 2. Lambda parameters
        BiFunction<Integer, Integer, Integer> f = (x, _) -> x * 2;
        System.out.println("Lambda result: " + f.apply(10, 5));

        // 3. Local variable
        int _ = 10;
        // System.out.println(_); // ❌ Does not compile
        // int x = _; // ❌ Does not compile

        // 4. For loop
        // Traditional for loop using _ as a counter is tricky because
        // increment/condition needs to read _, which is not allowed.
        // for (int _ = 0; _ < 1; _++) { // ❌ DOES NOT COMPILE
        //     System.out.println("For loop with unnamed index.");
        // }
        // The enhanced for loop is the correct way to use _ for iteration.
        for (String _ : List.of("A")) {
            System.out.println("Enhanced for loop with unnamed element.");
        }

        // 5. Enhanced for loop
        List<String> names = List.of("Alice", "Bob");
        for (String _ : names) {
            System.out.println("Enhanced for loop with unnamed element.");
        }

        // 6. Try-with-resources
        try (var res = new java.io.StringReader("test")) {
            System.out.println("Try-with-resources with named resource.");
        }
        
        try (var _ = new java.io.StringReader("test")) {
             System.out.println("Try-with-resources with unnamed resource.");
        }

        // 7. Lambda in stream
        Stream.of("a", "b").forEach(_ -> System.out.println("Lambda in stream"));

        // 8. Pattern Matching (instanceof)
        Object obj = "test";
        if (obj instanceof String _) {
            System.out.println("instanceof with String pattern.");
        }

        // 9. Record Pattern Matching (with _)
        Object p = new Point(1, 2);
        if (p instanceof Point(int x, _)) {
            System.out.println("Record pattern with _: x=" + x);
        }

        // if (obj instanceof Point(_, _)) { }
        // Let's test this.
        Object p2 = new Point(10, 20);
        if (p2 instanceof Point(_, _)) {
            System.out.println("Point(_, _) works!");
        }

        // --- Examples that DON'T compile (commented out) ---
        
        // 1. Referencing _
        // int _ = 5;
        // System.out.println(_); // ❌ Error: cannot find symbol

        // 2. Class/Instance variable
        // class Example {
        //     int _ = 10;      // ❌ Error
        //     static String _; // ❌ Error
        // }

        // 3. Universal wildcard
        // if (obj instanceof _) { } // ❌ Error
        // case _ -> ...             // ❌ Error
    }
}
