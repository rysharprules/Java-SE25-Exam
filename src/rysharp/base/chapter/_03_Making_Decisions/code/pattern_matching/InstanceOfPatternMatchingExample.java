package rysharp.base.chapter._03_Making_Decisions.code.pattern_matching;

/**
 * Demonstrates the main principles of pattern matching with instanceof.
 */
public class InstanceOfPatternMatchingExample {

    public static void main(String[] args) {

        Object text = "Hello Java";
        Object number = 42;
        Object nothing = null;

        /*
         * Traditional instanceof:
         *
         * We first test the type and then explicitly cast the object.
         */
        if (text instanceof String) {
            String value = (String) text;
            System.out.println(value.toUpperCase());
        }

        /*
         * Pattern matching:
         *
         * The type test and declaration/cast are combined.
         *
         * If text is a String, value is introduced as a String
         * and refers to the same object as text.
         */
        if (text instanceof String value) {
            System.out.println(value.toUpperCase());
            System.out.println(value.length());
        }

        /*
         * The declared type of the original variable can be broad.
         *
         * number has compile-time type Object, so testing whether
         * the object is an Integer is valid.
         */
        if (number instanceof Integer value) {
            System.out.println(value + 10);
        }

        /*
         * null never matches a type pattern.
         *
         * This simply evaluates to false. It does not throw a
         * NullPointerException.
         */
        if (nothing instanceof String value) {
            System.out.println(value);
        } else {
            System.out.println("null did not match String");
        }

        /*
         * Pattern variables only exist where the compiler knows
         * that the pattern successfully matched.
         *
         * This would NOT compile because value is not in scope:
         *
         * System.out.println(value);
         */
    }
}