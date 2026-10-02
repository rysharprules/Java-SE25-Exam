/*
 * Constant Expressions and Compound Assignment
 *
 * Covers:
 * - constant-expression narrowing
 * - why 7 * 10 can be assigned to byte
 * - ordinary variables vs constant variables
 * - final compile-time constants
 * - compound assignment
 * - ++ and --
 */

void main() {

    // ============================================================
    // 1. CONSTANT EXPRESSIONS
    // ============================================================

    byte a = 1;
    byte b = 7 * 10;
    short c = 2 + 1;
    char d = 65;

    System.out.println(a);
    System.out.println(b);  // 70
    System.out.println(c);  // 3
    System.out.println(d);  // A

    /*
     * IMPORTANT:
     *
     * Do NOT think:
     *
     *     "7 * 10 doesn't promote to int."
     *
     * It IS an int expression.
     *
     * Both literals are int:
     *
     *     int * int
     *         |
     *         v
     *        int
     *
     * But 7 * 10 is also a COMPILE-TIME CONSTANT EXPRESSION.
     *
     * The compiler calculates:
     *
     *     7 * 10 = 70
     *
     * Since 70 fits inside byte, assignment conversion permits
     * the constant int value to be narrowed to byte.
     */


    // ============================================================
    // 2. THE CONSTANT MUST FIT
    // ============================================================

    byte fits = 127;

    // byte tooLarge = 128;
    // DOES NOT COMPILE

    // byte alsoTooLarge = 7 * 100;
    // DOES NOT COMPILE

    /*
     * 7 * 100 is still a compile-time constant expression.
     *
     * The compiler calculates:
     *
     *     700
     *
     * But 700 does not fit inside byte.
     */


    // ============================================================
    // 3. ORDINARY VARIABLES
    // ============================================================

    byte hat = 1;

    // short boots = 2 + hat;
    // DOES NOT COMPILE

    /*
     * hat is an ordinary variable.
     *
     * Therefore:
     *
     *     2 + hat
     *
     * becomes:
     *
     *     int + byte
     *          |
     *          v
     *     int + int
     *          |
     *          v
     *         int
     *
     * This is not the special constant-expression assignment
     * case, so the resulting int cannot implicitly narrow
     * to short.
     */

    short boots = (short) (2 + hat);

    System.out.println("boots: " + boots);


    // ============================================================
    // 4. final CONSTANT VARIABLES
    // ============================================================

    final int size = 10;

    byte smallSize = size;
    byte biggerSize = size + 5;

    System.out.println("smallSize: " + smallSize);
    System.out.println("biggerSize: " + biggerSize);

    /*
     * This is why "literal vs variable" is not quite the
     * complete rule.
     *
     * size is a variable, but it is also a constant variable
     *
     * Therefore size can participate in compile-time
     * constant expressions.
     */


    // ============================================================
    // 5. REMOVE final AND THE RESULT CHANGES
    // ============================================================

    int normalSize = 10;

    // byte x = normalSize;
    // DOES NOT COMPILE

    // byte y = normalSize + 5;
    // DOES NOT COMPILE

    /*
     * A human can see normalSize currently contains 10.
     *
     * But normalSize is not a constant variable.
     *
     * Therefore the special constant-expression narrowing
     * does not apply.
     */


    // ============================================================
    // 6. COMPOUND ASSIGNMENT
    // ============================================================

    byte number = 10;

    // number = number + 1;
    // DOES NOT COMPILE

    /*
     * number + 1
     *
     * byte + int
     *     |
     *     v
     * int + int
     *     |
     *     v
     *    int
     *
     * int cannot implicitly narrow back to byte.
     */

    number += 1;

    System.out.println("After += 1: " + number);

    /*
     * Compound assignment includes an implicit conversion
     * back to the type of the left-hand variable.
     *
     * A useful conceptual model:
     *
     *     number += 1;
     *
     * behaves approximately like:
     *
     *     number = (byte) (number + 1);
     */


    // ============================================================
    // 7. COMPOUND ASSIGNMENT WITH A LARGER TYPE
    // ============================================================

    long goat = 10L;
    int sheep = 5;

    // sheep = sheep * goat;
    // DOES NOT COMPILE

    /*
     * int * long -> long
     *
     * Then:
     *
     * long -> int
     *
     * would be narrowing.
     */

    sheep *= goat;

    System.out.println("sheep: " + sheep);

    /*
     * Conceptually:
     *
     * sheep *= goat;
     *
     * behaves approximately like:
     *
     * sheep = (int) (sheep * goat);
     */


    // ============================================================
    // 8. ++ AND --
    // ============================================================

    byte counter = 10;

    counter++;
    ++counter;
    counter--;
    --counter;

    System.out.println("counter: " + counter);

    /*
     * All of these compile.
     *
     * Compare with:
     *
     *     counter = counter + 1;
     *
     * which DOES NOT COMPILE because counter + 1 is int.
     */


    // ============================================================
    // EXAM MEMORY MODEL
    // ============================================================

    /*
     * For an expression:
     *
     * 1. Determine the operand types.
     *
     * 2. Ask whether it is a compile-time constant expression.
     *
     * 3. Apply numeric promotion.
     *
     * 4. Determine the resulting expression type.
     *
     * 5. Apply any explicit casts.
     *
     * 6. Check whether the result can be assigned to the
     *    variable on the left.
     *
     * 7. If compound assignment is used, remember that it
     *    includes conversion back to the LHS type.
     *
     *
     * Most important distinction:
     *
     *     byte b = 7 * 10;
     *
     * works NOT because promotion was skipped.
     *
     * 7 * 10 is an int expression.
     *
     * It works because it is a compile-time constant expression
     * whose value (70) fits inside byte.
     */
}