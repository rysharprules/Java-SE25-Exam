package rysharp.jdk25.addendum.Minor_Api_Additions.code;

import java.io.Console;
import java.util.Locale;

public class ConsoleLocaleExample {

    public static void main(String[] args) {

        /*
         * System.console() is NOT guaranteed to return a Console.
         *
         * It commonly returns null when run from an IDE, so this example
         * checks for null to remain safely runnable in different environments.
         */
        Console console = System.console();

        if (console == null) {
            System.out.println(
                    "No console is available. Run from a terminal to see the example.");
            return;
        }

        /*
         * Java 23 added Locale-aware Console formatting overloads.
         *
         * Locale controls formatting conventions. It does NOT translate
         * the literal text "Value".
         */
        console.printf(Locale.US,
                "Value: %,.2f%n", 1234.5);

        console.printf(Locale.GERMANY,
                "Value: %,.2f%n", 1234.5);

        // US:      Value: 1,234.50
        // Germany: Value: 1.234,50

        /*
         * Locale also applies when formatting a readLine() prompt.
         *
         * IMPORTANT:
         * Locale affects the PROMPT, not how the entered String is parsed.
         *
         * Typing 1234.50 returns "1234.50".
         * Typing 1234,50 returns "1234,50".
         */
        String input = console.readLine(
                Locale.GERMANY,
                "Enter a value (example: %,.2f): ",
                1234.5);

        console.printf("You entered: %s%n", input);
    }
}