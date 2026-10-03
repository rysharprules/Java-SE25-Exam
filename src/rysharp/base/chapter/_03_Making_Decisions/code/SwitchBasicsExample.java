/*
 * Switch Basics
 *
 * Covers:
 * - traditional switch statements
 * - fall-through
 * - break
 * - arrow labels
 * - multiple case values
 * - default
 *
 * Java 25
 */

void main() {

    // ============================================================
    // 1. TRADITIONAL SWITCH STATEMENT
    // ============================================================

    int day = 2;

    switch (day) {
        case 1:
            System.out.println("Monday");
            break;
        case 2:
            System.out.println("Tuesday");
            break;
        case 3:
            System.out.println("Wednesday");
            break;
        default: // optional here. E.g. if commented out and 4 is passed, there is no output.
            System.out.println("Other day");
    }

    /*
     * Traditional ':' case labels can fall through.
     *
     * break exits the switch.
     */


    // ============================================================
    // 2. FALL-THROUGH
    // ============================================================

    int number = 1;

    switch (number) {
        case 1:
            System.out.println("One");
            // No break!

        case 2:
            System.out.println("Two");

        case 3:
            System.out.println("Three");
            break;

        default:
            System.out.println("Other");
    }

    /*
     * Output:
     *
     * One
     * Two
     * Three
     *
     * Once case 1 matches, execution continues through
     * subsequent ':' labels until break or the switch ends.
     */


    // ============================================================
    // 3. ARROW LABELS
    // ============================================================

    int month = 2;

    switch (month) {
        case 1 -> System.out.println("January");
        case 2 -> System.out.println("February");
        case 3 -> System.out.println("March");
        default -> System.out.println("Other month");
    }

    /*
     * Arrow labels do NOT fall through.
     *
     * No break is required.
     */


    // ============================================================
    // 4. MULTIPLE VALUES FOR ONE CASE
    // ============================================================

    int dayOfWeek = 6;

    switch (dayOfWeek) {
        case 1, 2, 3, 4, 5 ->
                System.out.println("Weekday");

        case 6, 7 ->
                System.out.println("Weekend");

        default ->
                System.out.println("Invalid");
    }


    // ============================================================
    // 5. STRING SWITCH
    // ============================================================

    String animal = "dog";

    switch (animal) {
        case "dog" ->
                System.out.println("Woof");

        case "cat" ->
                System.out.println("Meow");

        default ->
                System.out.println("Unknown animal");
    }


    // ============================================================
    // 6. ENUM SWITCH
    // ============================================================

    Season season = Season.SUMMER;

    switch (season) {
        case SPRING ->
                System.out.println("Spring");

        case SUMMER ->
                System.out.println("Summer");

        case Season.AUTUMN ->
                System.out.println("Autumn");

        case WINTER ->
                System.out.println("Winter");
    }

    /*
     * Notice that enum constants are written:
     *
     *     case SUMMER
     *
     * NOT:
     *
     *     case Season.SUMMER
     *
     * but it is legal to do so
     */
}

enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER
}