package rysharp.base.chapter._04_Core_APIs.code;

import java.util.Arrays;

public class StringExample {

    public static void main(String[] args) {

        // 1. String immutability
        String a = "Hello";
        String b = a;

        a.concat(" World"); // Result discarded

        System.out.println("1. " + a); // Hello
        System.out.println("2. " + b); // Hello

        // 2. concat() returns a new String
        a = a.concat(" World");

        System.out.println("3. " + a); // Hello World
        System.out.println("4. " + b); // Hello

        // 3. Concatenation using +
        String c = "Java";
        String d = c + " 25";

        System.out.println("5. " + c); // Java
        System.out.println("6. " + d); // Java 25

        // 4. Concatenation using +=
        String e = "Good";
        String f = e;

        e += " Morning";

        System.out.println("7. " + e); // Good Morning
        System.out.println("8. " + f); // Good

        // 5. String pool: literals
        String s1 = "Java";
        String s2 = "Java";

        System.out.println("9. " + (s1 == s2));      // true
        System.out.println("10. " + s1.equals(s2)); // true

        // 6. new String() creates a separate object
        String s3 = new String("Java");

        System.out.println("11. " + (s1 == s3));      // false
        System.out.println("12. " + s1.equals(s3)); // true

        // 7. Compile-time constant concatenation
        String s4 = "Ja" + "va";

        System.out.println("13. " + (s1 == s4)); // true

        // 8. Runtime concatenation
        String part = "Ja";
        String s5 = part + "va";

        System.out.println("14. " + (s1 == s5));      // false
        System.out.println("15. " + s1.equals(s5)); // true

        // 9. intern() returns the pooled reference
        String s6 = s5.intern();

        System.out.println("16. " + (s1 == s6)); // true

        // 10. final compile-time constants
        final String prefix = "Ja";
        String s7 = prefix + "va";

        System.out.println("17. " + (s1 == s7)); // true

        // 11. concat() versus + with null
        String text = "Hello";
        String nullText = null;

        System.out.println("18. " + (text + nullText)); // Hellonull

        // text.concat(nullText);
        // Would throw NullPointerException

        example12_splitWithDelimiters();
    }


    private static void example12_splitWithDelimiters() {
        System.out.println("\n12. splitWithDelimiters()");

        String text = "boo:::and::foo::poo::moo";

        // split() discards delimiters.
        System.out.println(Arrays.toString(text.split(":+")));
        // [boo, and, foo, poo, moo]

        // splitWithDelimiters() retains matched delimiters.
        System.out.println(Arrays.toString(text.splitWithDelimiters(":+", -1)));
        // [boo, :::, and, ::, foo, ::, poo, ::, moo]

        // Positive limit: at most limit - 1 delimiter matches.
        System.out.println(Arrays.toString(text.splitWithDelimiters(":+", 2)));
        // [boo, :::, and::foo::poo::moo]

        System.out.println(Arrays.toString(text.splitWithDelimiters(":+", 3)));
        // [boo, :::, and, ::, foo::poo::moo]

        // Zero: unlimited splitting; trailing empty strings removed.
        System.out.println(Arrays.toString("a:b:".splitWithDelimiters(":", 0)));
        // [a, :, b, :]

        // Negative: unlimited splitting; trailing empty strings preserved.
        System.out.println(Arrays.toString("a:b:".splitWithDelimiters(":", -1)));
        // [a, :, b, :, ]
    }

}
