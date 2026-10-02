/*
 * Precedence vs Evaluation Order
 *
 * This is an important exam distinction:
 *
 * PRECEDENCE
 *     determines how an expression is GROUPED.
 *
 * EVALUATION ORDER
 *     determines which operand is actually evaluated first.
 *
 * Java evaluates operands left-to-right.
 *
 * These are NOT the same concept.
 */

void main() {

    // ============================================================
    // 1. PREFIX ++
    // ============================================================

    int a = 5;

    int b = ++a;

    System.out.println("a = " + a); // 6
    System.out.println("b = " + b); // 6

    /*
     * ++a means:
     *
     *     1. increment a
     *     2. produce the NEW value
     *
     * Therefore:
     *
     *     a = 6
     *     expression value = 6
     */


    // ============================================================
    // 2. POSTFIX ++
    // ============================================================

    int c = 5;

    int d = c++;

    System.out.println("c = " + c); // 6
    System.out.println("d = " + d); // 5

    /*
     * c++ means:
     *
     *     1. produce the OLD value
     *     2. increment c
     *
     * Therefore:
     *
     *     expression value = 5
     *     c becomes 6
     */


    // ============================================================
    // 3. PREFIX AND POSTFIX IN ONE EXPRESSION
    // ============================================================

    int x = 2;

    int y = x++ + ++x * 2;

    System.out.println("x = " + x); // 4
    System.out.println("y = " + y); // 10

    /*
     * FIRST: apply precedence.
     *
     * Multiplication has higher precedence than addition:
     *
     *     x++ + (++x * 2)
     *
     *
     * SECOND: evaluate operands LEFT TO RIGHT.
     *
     * Start:
     *
     *     x = 2
     *
     *
     * Evaluate left operand of +:
     *
     *     x++
     *
     * expression produces 2
     * x becomes 3
     *
     *
     * Now evaluate the right operand:
     *
     *     ++x * 2
     *
     * ++x:
     *
     *     x becomes 4
     *     expression produces 4
     *
     * Then:
     *
     *     4 * 2 = 8
     *
     *
     * Finally:
     *
     *     2 + 8 = 10
     *
     *
     * Final:
     *
     *     x = 4
     *     y = 10
     */


    // ============================================================
    // 4. PRECEDENCE DOES NOT MEAN "EXECUTES FIRST"
    // ============================================================

    int result = first() + second() * third();

    System.out.println("result = " + result);

    /*
     * Precedence groups this as:
     *
     *     first() + (second() * third())
     *
     * BUT Java evaluates operands left-to-right.
     *
     * Therefore the methods are called:
     *
     *     first()
     *     second()
     *     third()
     *
     * NOT:
     *
     *     second()
     *     third()
     *     first()
     *
     *
     * With the values below:
     *
     *     first()  -> 2
     *     second() -> 3
     *     third()  -> 4
     *
     * Result:
     *
     *     2 + (3 * 4)
     *
     *     2 + 12
     *
     *     14
     */


    // ============================================================
    // 5. LEFT-TO-RIGHT CAN MATTER WITH SIDE EFFECTS
    // ============================================================

    int number = 1;

    int answer = number++ + number++;

    System.out.println("number = " + number); // 3
    System.out.println("answer = " + answer); // 3

    /*
     * Start:
     *
     *     number = 1
     *
     * Left operand:
     *
     *     number++
     *
     * produces 1
     * number becomes 2
     *
     *
     * Right operand:
     *
     *     number++
     *
     * produces 2
     * number becomes 3
     *
     *
     * Addition:
     *
     *     1 + 2 = 3
     *
     * Final:
     *
     *     number = 3
     *     answer = 3
     */


    // ============================================================
    // 6. SHORT-CIRCUIT &&
    // ============================================================

    int counter = 0;

    boolean andResult =
            false && ++counter > 0;

    System.out.println("andResult = " + andResult); // false
    System.out.println("counter = " + counter);     // 0

    /*
     * && short-circuits.
     *
     * Once the left operand is false, the complete expression
     * must be false.
     *
     * Therefore:
     *
     *     ++counter
     *
     * is NEVER evaluated.
     */


    // ============================================================
    // 7. SHORT-CIRCUIT ||
    // ============================================================

    boolean orResult =
            true || ++counter > 0;

    System.out.println("orResult = " + orResult); // true
    System.out.println("counter = " + counter);   // 0

    /*
     * || short-circuits.
     *
     * Once the left operand is true, the complete expression
     * must be true.
     *
     * The right operand is not evaluated.
     */


    // ============================================================
    // 8. & AND | DO NOT SHORT-CIRCUIT
    // ============================================================

    boolean normalAnd =
            false & ++counter > 0;

    System.out.println("normalAnd = " + normalAnd); // false
    System.out.println("counter = " + counter);     // 1

    /*
     * Boolean & evaluates BOTH operands.
     *
     * Therefore ++counter executes even though the
     * left operand is already false.
     */

    boolean normalOr =
            true | ++counter > 0;

    System.out.println("normalOr = " + normalOr); // true
    System.out.println("counter = " + counter);   // 2

    /*
     * Boolean | also evaluates BOTH operands.
     */


    // ============================================================
    // 9. TERNARY ONLY EVALUATES ONE RESULT BRANCH
    // ============================================================

    int value = 10;

    String text =
            value > 5
                    ? trueBranch()
                    : falseBranch();

    System.out.println(text);

    /*
     * value > 5 is true.
     *
     * Therefore trueBranch() executes.
     *
     * falseBranch() does NOT execute.
     *
     * Like && and ||, the ternary operator does not
     * necessarily evaluate every expression appearing in it.
     */


    // ============================================================
    // EXAM METHOD
    // ============================================================

    /*
     * When faced with a nasty expression:
     *
     * STEP 1
     *
     * Add imaginary parentheses according to PRECEDENCE.
     *
     *
     * STEP 2
     *
     * Within that grouping, evaluate operands LEFT TO RIGHT.
     *
     *
     * STEP 3
     *
     * For every ++ or --, keep track of TWO things:
     *
     *     - value produced by the expression
     *     - new value stored in the variable
     *
     *
     * PREFIX:
     *
     *     ++x
     *
     *     change first
     *     produce NEW value
     *
     *
     * POSTFIX:
     *
     *     x++
     *
     *     produce OLD value
     *     variable still changes
     *
     *
     * STEP 4
     *
     * Check for short-circuiting:
     *
     *     &&   may skip right operand
     *     ||   may skip right operand
     *
     *     &    evaluates both
     *     |    evaluates both
     *
     *
     * BEST MEMORY RULE:
     *
     *     PRECEDENCE = GROUPING
     *
     *     LEFT-TO-RIGHT = EVALUATION
     */
}


/*
 * Helper methods deliberately print when they execute so that
 * evaluation order can be seen.
 */

int first() {
    System.out.println("first()");
    return 2;
}

int second() {
    System.out.println("second()");
    return 3;
}

int third() {
    System.out.println("third()");
    return 4;
}

String trueBranch() {
    System.out.println("trueBranch()");
    return "TRUE branch selected";
}

String falseBranch() {
    System.out.println("falseBranch()");
    return "FALSE branch selected";
}