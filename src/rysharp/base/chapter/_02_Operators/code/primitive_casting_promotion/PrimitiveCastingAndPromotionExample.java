/*
 * Primitive Casting and Numeric Promotion
 *
 * Covers:
 * - widening
 * - narrowing
 * - floating-point -> integral conversion
 * - binary numeric promotion
 * - byte/short/char promotion to int
 * - mixed numeric types
 * - cast placement
 * - overflow caused by narrowing
 * - literal suffixes
 */

void main() {

    // ============================================================
    // 1. WIDENING
    // ============================================================

    byte byteValue = 10;

    short shortValue = byteValue;
    int intValue = shortValue;
    long longValue = intValue;
    float floatValue = longValue;
    double doubleValue = floatValue;

    System.out.println("Widened byte to double: " + doubleValue);

    /*
     * Widening normally happens automatically.
     *
     * byte -> short -> int -> long -> float -> double
     *
     * char can also widen to int and beyond.
     */


    // ============================================================
    // 2. NARROWING
    // ============================================================

    double price = 19.99;

    // int wholePrice = price;
    // DOES NOT COMPILE:
    // double cannot implicitly narrow to int.

    int wholePrice = (int) price;

    System.out.println("19.99 cast to int: " + wholePrice);

    /*
     * Output:
     *
     * 19
     *
     * Floating-point -> integral conversion truncates.
     * It does NOT round.
     */


    // ============================================================
    // 3. byte, short AND char PROMOTE TO int DURING ARITHMETIC
    // ============================================================

    short mouse = 10;
    short hamster = 3;

    // short result = mouse * hamster;
    // DOES NOT COMPILE

    int result = mouse * hamster;

    System.out.println("short * short: " + result);

    /*
     * mouse * hamster
     *
     * short * short
     *      |
     *      v
     * int * int
     *      |
     *      v
     *     int
     */


    // ============================================================
    // 4. CAST THE RESULT BACK IF REQUIRED
    // ============================================================

    short shortResult = (short) (mouse * hamster);

    System.out.println("Cast result back to short: " + shortResult);


    // ============================================================
    // 5. CAST PLACEMENT MATTERS
    // ============================================================

    // short wrong = (short) mouse * hamster;
    // DOES NOT COMPILE

    /*
     * The cast above applies ONLY to mouse:
     *
     * (short) mouse * hamster
     *
     * becomes:
     *
     * short * short
     *      |
     *      v
     *     int
     *
     * Therefore the final result is still int.
     */

    short correct = (short) (mouse * hamster);

    System.out.println("Correct cast placement: " + correct);


    // ============================================================
    // 6. A CAST DOES NOT PROTECT LATER ARITHMETIC
    // ============================================================

    // short another =
    //         1 + (short) (mouse * hamster);
    //
    // DOES NOT COMPILE

    /*
     * First:
     *
     * (short) (mouse * hamster)
     *
     * produces a short.
     *
     * But then:
     *
     * 1 + short
     *
     * becomes:
     *
     * int + int
     *     |
     *     v
     *    int
     *
     * Always reconsider promotion when another arithmetic
     * operation takes place.
     */

    short another =
            (short) (1 + (short) (mouse * hamster));

    System.out.println("Cast after final operation: " + another);


    // ============================================================
    // 7. MIXED NUMERIC TYPES
    // ============================================================

    int count = 5;
    long total = 10L;

    long mixedResult = count + total;

    // int badMixedResult = count + total;
    // DOES NOT COMPILE

    int castMixedResult = (int) (count + total);

    System.out.println("int + long as long: " + mixedResult);
    System.out.println("int + long cast to int: " + castMixedResult);

    /*
     * int + long
     *     |
     *     v
     * long + long
     *     |
     *     v
     *    long
     *
     *
     * Useful promotion order:
     *
     * double
     *   ^
     * float
     *   ^
     * long
     *   ^
     * int
     */


    // ============================================================
    // 8. char ALSO PROMOTES TO int
    // ============================================================

    char letter = 'A';

    int nextValue = letter + 1;

    System.out.println("'A' + 1: " + nextValue);

    /*
     * 'A' has numeric value 65.
     *
     * char + int
     *     |
     *     v
     * int + int
     *
     * Result = 66
     */

    char nextLetter = (char) (letter + 1);

    System.out.println("Cast back to char: " + nextLetter); // B


    // ============================================================
    // 9. FLOATING-POINT LITERALS
    // ============================================================

    double defaultFloatingPoint = 2.0;

    // float badFloat = 2.0;
    // DOES NOT COMPILE:
    // 2.0 is a double literal.

    float explicitFloat = 2.0F;
    float castFloat = (float) 2.0;

    System.out.println(defaultFloatingPoint);
    System.out.println(explicitFloat);
    System.out.println(castFloat);


    // ============================================================
    // 10. INTEGER LITERALS
    // ============================================================

    long smallLong = 100;

    /*
     * 100 is an int literal.
     *
     * int -> long is widening, so this is fine.
     */

    long largeLong = 192301398193810323L;

    /*
     * A sufficiently large integer literal needs L.
     *
     * This would NOT compile:
     *
     * long bad = 192301398193810323;
     *
     * Neither would:
     *
     * long bad = (long) 192301398193810323;
     *
     * The literal itself must be valid before the cast
     * can be applied.
     */

    System.out.println("Small long: " + smallLong);
    System.out.println("Large long: " + largeLong);


    // ============================================================
    // 11. NARROWING CAN LOSE INFORMATION
    // ============================================================

    int large = 130;

    byte narrowed = (byte) large;

    System.out.println("130 cast to byte: " + narrowed);

    /*
     * Output:
     *
     * -126
     *
     * byte range:
     *
     * -128 to 127
     *
     * An explicit cast allows the conversion but does NOT
     * guarantee that the original value fits.
     *
     * Exam lesson:
     *
     * "Does this compile?"
     *
     * and
     *
     * "What value does this produce?"
     *
     * are separate questions.
     */
}