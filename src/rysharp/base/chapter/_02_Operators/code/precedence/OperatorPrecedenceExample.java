/*
 * Operator Precedence
 *
 * Covers:
 * - precedence
 * - parentheses
 * - associativity
 * - arithmetic operators
 * - relational/equality operators
 * - logical operators
 * - ternary
 * - assignment
 *
 * IMPORTANT:
 *
 * Precedence determines GROUPING.
 * It does NOT generally determine evaluation order.
 */

void main() {

    // ============================================================
    // 1. MULTIPLICATION BEFORE ADDITION
    // ============================================================

    int result = 2 + 3 * 4;

    System.out.println(result); // 14

    /*
     * Multiplication has higher precedence than addition.
     *
     * Therefore:
     *
     *     2 + 3 * 4
     *
     * groups as:
     *
     *     2 + (3 * 4)
     *
     * NOT:
     *
     *     (2 + 3) * 4
     */


    // ============================================================
    // 2. PARENTHESES OVERRIDE NORMAL PRECEDENCE
    // ============================================================

    int result2 = (2 + 3) * 4;

    System.out.println(result2); // 20

    /*
     * Parentheses force 2 + 3 to be evaluated as a group.
     */


    // ============================================================
    // 3. SAME PRECEDENCE -> ASSOCIATIVITY
    // ============================================================

    int subtraction = 20 - 5 - 3;

    System.out.println(subtraction); // 12

    /*
     * + and - are left-associative.
     *
     * Therefore:
     *
     *     20 - 5 - 3
     *
     * groups as:
     *
     *     (20 - 5) - 3
     *
     *     15 - 3
     *
     *     12
     *
     * NOT:
     *
     *     20 - (5 - 3)
     *
     * which would be 18.
     */


    // ============================================================
    // 4. DIVISION IS ALSO LEFT-ASSOCIATIVE
    // ============================================================

    int division = 100 / 10 / 2;

    System.out.println(division); // 5

    /*
     * Groups as:
     *
     *     (100 / 10) / 2
     *
     *     10 / 2
     *
     *     5
     */


    // ============================================================
    // 5. UNARY OPERATORS HAVE HIGH PRECEDENCE
    // ============================================================

    int x = 5;

    int negative = -x * 2;

    System.out.println(negative); // -10

    /*
     * Unary - has higher precedence than multiplication.
     *
     * Groups as:
     *
     *     (-x) * 2
     */


    // ============================================================
    // 6. RELATIONAL BEFORE EQUALITY
    // ============================================================

    boolean comparison = 5 < 10 == true;

    System.out.println(comparison); // true

    /*
     * < has higher precedence than ==.
     *
     * Groups as:
     *
     *     (5 < 10) == true
     *
     *     true == true
     *
     *     true
     */


    // ============================================================
    // 7. && BEFORE ||
    // ============================================================

    boolean logical =
            true || false && false;

    System.out.println(logical); // true

    /*
     * && has higher precedence than ||.
     *
     * Groups as:
     *
     *     true || (false && false)
     *
     *     true || false
     *
     *     true
     */


    // ============================================================
    // 8. BITWISE BOOLEAN OPERATORS
    // ============================================================

    boolean bitwise =
            true | false & false;

    System.out.println(bitwise); // true

    /*
     * Among these operators:
     *
     *     &
     *     ^
     *     |
     *
     * precedence is:
     *
     *     &
     *     ^
     *     |
     *
     * Therefore:
     *
     *     true | false & false
     *
     * groups as:
     *
     *     true | (false & false)
     */


    // ============================================================
    // 9. TERNARY OPERATOR
    // ============================================================

    int age = 20;

    String status =
            age >= 18 ? "Adult" : "Minor";

    System.out.println(status); // Adult

    /*
     * The condition is evaluated first:
     *
     *     age >= 18
     *
     * Then ONE of the two result expressions is selected.
     */


    // ============================================================
    // 10. ASSIGNMENT HAS LOW PRECEDENCE
    // ============================================================

    int number;

    number = 2 + 3 * 4;

    System.out.println(number); // 14

    /*
     * Arithmetic happens before assignment.
     *
     * Conceptually:
     *
     *     number = (2 + (3 * 4));
     */


    // ============================================================
    // 11. ASSIGNMENT IS RIGHT-ASSOCIATIVE
    // ============================================================

    int a;
    int b;
    int c;

    a = b = c = 10;

    System.out.println(a); // 10
    System.out.println(b); // 10
    System.out.println(c); // 10

    /*
     * Assignment is right-associative.
     *
     * Groups as:
     *
     *     a = (b = (c = 10))
     */


    // ============================================================
    // EXAM MEMORY TABLE
    // ============================================================

    /*
     * Useful precedence order from HIGH to LOW:
     *
     * postfix:
     *     expr++  expr--
     *
     * prefix / unary:
     *     ++  --  +  -  !  ~
     *
     * multiplicative:
     *     *  /  %
     *
     * additive:
     *     +  -
     *
     * shift:
     *     <<  >>  >>>
     *
     * relational:
     *     <  <=  >  >=  instanceof
     *
     * equality:
     *     ==  !=
     *
     * bitwise AND:
     *     &
     *
     * bitwise XOR:
     *     ^
     *
     * bitwise OR:
     *     |
     *
     * logical AND:
     *     &&
     *
     * logical OR:
     *     ||
     *
     * ternary:
     *     ? :
     *
     * assignment:
     *     =  +=  -=  *=  /=  etc.
     *
     */
}