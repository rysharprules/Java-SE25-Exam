/*
 * Pattern Matching for switch
 *
 * Covers:
 * - type patterns
 * - null
 * - guards with when
 * - dominance
 * - exhaustive pattern switches
 * - case null, default
 *
 * Java 25
 *
 * This example uses normal, non-preview Java 25 features.
 */

void main() {

    // ============================================================
    // 1. TYPE PATTERNS
    // ============================================================

    Object value = "Java";

    String result = switch (value) {

        case String s ->
                "String: " + s;

        case Integer i ->
                "Integer: " + i;

        default ->
                "Something else";
    };

    System.out.println(result);

    /*
     * The pattern:
     *
     *     case String s
     *
     * both:
     *
     * 1. tests whether the object is a String
     * 2. creates pattern variable s
     */


    // ============================================================
    // 2. NULL HANDLING
    // ============================================================

    Object nullable = null;

    String nullResult = switch (nullable) {

        case null ->
                "Null";

        case String s ->
                "String: " + s;

        default ->
                "Something else";
    };

    System.out.println(nullResult);

    /*
     * case null explicitly handles null.
     *
     * IMPORTANT:
     *
     * default does NOT generally mean:
     *
     *     "including null"
     */


    // ============================================================
    // 3. case null CAN APPEAR BEFORE OTHER CASES
    // ============================================================

    Object another = "Hello";

    String anotherResult = switch (another) {

        case null ->
                "Null";

        case Integer i ->
                "Integer";

        case String s ->
                "String";

        default ->
                "Other";
    };

    System.out.println(anotherResult);


    // ============================================================
    // 4. GUARDS WITH when
    // ============================================================

    Object guarded = "Hello Java";

    String guardedResult = switch (guarded) {

        case String s when s.length() > 10 ->
                "Long string";

        case String s ->
                "Other string";

        default ->
                "Not a string";
    };

    System.out.println(guardedResult);

    /*
     * A guard adds an additional condition:
     *
     *     case String s when condition
     *
     *
     * Put the more specific guarded case BEFORE the
     * general String case.
     */


    // ============================================================
    // 5. DOMINANCE
    // ============================================================

    Object dominance = "Java";

    String dominanceResult = switch (dominance) {

        case String s ->
                "String";

        case Object o ->
                "Other object";
    };

    System.out.println(dominanceResult);

    /*
     * Order matters with patterns.
     *
     * This would NOT compile:
     *
     *     case Object o ->
     *         "Object";
     *
     *     case String s ->
     *         "String";
     *
     * Object would already match every String.
     *
     * Therefore the String pattern would be dominated.
     *
     *
     * Think:
     *
     *     specific patterns
     *           ↓
     *     general patterns
     */


    // ============================================================
    // 6. A TOTAL PATTERN CAN MAKE THE SWITCH EXHAUSTIVE
    // ============================================================

    Object anything = 42;

    String anythingResult = switch (anything) {

        case String s ->
                "String";

        case Object o ->
                "Some object";
    };

    System.out.println(anythingResult);

    /*
     * Object covers every non-null Object value.
     *
     * A matching type pattern can therefore contribute to
     * exhaustiveness without default.
     */


    // ============================================================
    // 7. COMBINED case null, default
    // ============================================================

    Object combined = null;

    String combinedResult = switch (combined) {

        case String s ->
                "String";

        case Integer i ->
                "Integer";

        case null, default ->
                "Everything else";
    };

    System.out.println(combinedResult);

    /*
     * case null, default
     *
     * handles null AND anything not handled above.
     *
     * IMPORTANT EXAM DETAIL:
     *
     * The combined:
     *
     *     case null, default
     *
     * must be the final case.
     */


    // ============================================================
    // 8. ENHANCED SWITCH STATEMENTS
    // ============================================================

    Object statementValue = "Java";

    switch (statementValue) {

        case String s ->
                System.out.println(s);

        case Integer i ->
                System.out.println(i);

        default ->
                System.out.println("Other");
    }

    /*
     * Pattern matching isn't limited to switch expressions.
     *
     * It can also be used with switch statements.
     *
     * A switch statement that uses patterns (or a null case)
     * is an ENHANCED switch statement and must be exhaustive.
     */


    // ============================================================
    // EXAM MEMORY MODEL
    // ============================================================

    /*
     * TYPE PATTERN
     *
     *     case String s
     *
     *
     * GUARDED PATTERN
     *
     *     case String s when condition
     *
     *
     * NULL
     *
     *     case null
     *
     *
     * DOMINANCE
     *
     *     specific before general
     *
     *
     * DEFAULT
     *
     *     fallback when no case matches
     *
     *     do NOT think of default as pattern dominance.
     *
     *
     * COMBINED
     *
     *     case null, default
     *
     *     must be last.
     */
}