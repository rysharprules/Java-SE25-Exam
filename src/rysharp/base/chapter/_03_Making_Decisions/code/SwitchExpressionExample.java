/*
 * Switch Expressions
 *
 * Covers:
 * - switch expressions
 * - exhaustiveness
 * - default
 * - exhaustive enums
 * - yield
 * - arrow blocks
 * - ':' syntax inside expressions
 *
 * Java 25
 */

void main() {

    // ============================================================
    // 1. SWITCH EXPRESSION
    // ============================================================

    int day = 2;

    String name = switch (day) {
        case 1 -> "Monday";
        case 2 -> "Tuesday";
        case 3 -> "Wednesday";
        default -> "Other";
    };

    System.out.println(name);


    // ============================================================
    // 2. SWITCH EXPRESSIONS MUST BE EXHAUSTIVE
    // ============================================================

    int value = 10;

    String description = switch (value) {
        case 0 -> "Zero";
        case 1 -> "One";
        default -> "Something else";
    };

    /*
     * A switch expression must produce a result for every
     * possible selector value.
     *
     * default is often used to make it exhaustive.
     */


    // ============================================================
    // 3. EXHAUSTIVE ENUM
    // ============================================================

    TrafficLight light = TrafficLight.RED;

    String action = switch (light) {
        case RED -> "Stop";
        case AMBER -> "Prepare";
        case GREEN -> "Go";
    };

    System.out.println(action);

    /*
     * No default is required here because every enum constant
     * has been covered.
     */


    // ============================================================
    // 4. default MAY STILL BE PRESENT
    // ============================================================

    int number = switch (light) {
        case RED -> 1;
        case AMBER -> 2;
        case GREEN -> 3;
        default -> 0;
    };

    /*
     * This compiles.
     *
     * The enum cases already make the switch exhaustive,
     * but adding default is still legal.
     *
     * Do NOT confuse:
     *
     *     exhaustive
     *
     * with:
     *
     *     unreachable
     */


    // ============================================================
    // 5. ARROW BLOCKS
    // ============================================================

    int score = 2;

    String result = switch (score) {

        case 1 -> {
            System.out.println("Processing score 1");
            yield "Low";
        }

        case 2 -> {
            System.out.println("Processing score 2");
            yield "Medium";
        }

        default -> {
            System.out.println("Processing another score");
            yield "High";
        }
    };

    System.out.println(result);

    /*
     * An arrow expression can simply provide its value:
     *
     *     case 1 -> "Low";
     *
     * But if an arrow uses a BLOCK and the switch needs a
     * result, use:
     *
     *     yield value;
     *
     * NOT:
     *
     *     return value;
     */


    // ============================================================
    // 6. TRADITIONAL ':' LABELS IN A SWITCH EXPRESSION
    // ============================================================

    int size = 2;

    String sizeName = switch (size) {

        case 1:
            yield "Small";

        case 2:
            yield "Medium";

        default: // despite using :, this is still an expression so default is required for this to be exhaustive
            yield "Large";
    };

    System.out.println(sizeName);

    /*
     * yield returns a value from the switch expression.
     *
     * break exits a switch statement.
     *
     * Do not confuse the two.
     */


    // ============================================================
    // 7. RESULT TYPES MUST BE COMPATIBLE
    // ============================================================

    int choice = 1;

    Number numericResult = switch (choice) {
        case 1 -> 10;      // Integer
        case 2 -> 20L;     // Long
        default -> 3.14;   // Double
    };

    System.out.println(numericResult);


    // ============================================================
    // EXAM MEMORY
    // ============================================================

    /*
     * SWITCH STATEMENT:
     *
     *     performs actions
     *
     *
     * SWITCH EXPRESSION:
     *
     *     produces a value
     *     MUST be exhaustive
     *
     *
     * ARROW:
     *
     *     case X -> ...
     *
     *     no fall-through
     *
     *
     * COLON:
     *
     *     case X:
     *
     *     can fall through
     *
     *
     * YIELD:
     *
     *     produces a value from a switch-expression block.
     */
}

enum TrafficLight {
    RED,
    AMBER,
    GREEN
}