package rysharp.base.chapter._03_Making_Decisions.code.pattern_matching;

/**
 * Demonstrates flow scoping of instanceof pattern variables.
 *
 * Main exam rule:
 *
 * A pattern variable is in scope wherever the compiler can prove
 * that the pattern must have successfully matched.
 */
public class FlowScopingExample {

    public static void main(String[] args) {

        testAnd("Hello");
        testAnd("Hi");
        testAnd(123);

        System.out.println();

        testEarlyReturn("Java");
        testEarlyReturn(42);
    }

    private static void testAnd(Object obj) {

        /*
         * && works because it evaluates from left to right.
         *
         * s.length() is only evaluated if:
         *
         *     obj instanceof String s
         *
         * was true.
         *
         * Therefore s definitely exists on the right side of &&.
         */
        if (obj instanceof String s && s.length() > 3) {
            System.out.println("Long String: " + s);
        }

        /*
         * Reversing the expression would NOT compile:
         *
         * if (s.length() > 3 && obj instanceof String s) {
         *     System.out.println(s);
         * }
         *
         * s has not been introduced when s.length() is evaluated.
         */

        /*
         * || is different.
         *
         * This would NOT compile:
         *
         * if (obj instanceof String s || s.length() > 3) {
         *     System.out.println(s);
         * }
         *
         * The right side of || is evaluated when the left side
         * is FALSE.
         *
         * That means s would be used precisely when the pattern
         * may NOT have matched.
         */
    }

    private static void testEarlyReturn(Object obj) {

        /*
         * This is the important flow-scoping example.
         *
         * If obj is NOT a String, this method returns.
         */
        if (!(obj instanceof String s)) {
            System.out.println("Not a String");
            return;
        }

        /*
         * If execution reaches here, Java knows the pattern
         * must have matched.
         *
         * Therefore s is still in scope even though we are
         * outside the if statement.
         */
        System.out.println("String: " + s);
        System.out.println("Length: " + s.length());
    }
}