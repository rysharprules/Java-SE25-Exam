/*
 * Demonstrates:
 *
 *  - Incidental whitespace
 *  - Essential whitespace
 *  - Common indentation
 *  - Closing delimiter indentation
 *  - Trailing whitespace
 *  - \s
 *
 * The helper method replaces spaces with · and
 * newlines with visible markers so whitespace
 * can actually be seen.
 */

void main() {

    System.out.println("===== INCIDENTAL VS ESSENTIAL =====");

    String text = """
            Java
              Study Guide
                Java 25
            """;

    showWhitespace(text);

    /*
     * Source:
     *
     *             Java
     *               Study Guide
     *                 Java 25
     *
     * The common indentation is incidental and removed.
     *
     * Result:
     *
     * Java
     *   Study Guide
     *     Java 25
     */


    System.out.println();
    System.out.println("===== CLOSING DELIMITER CONTROLS MARGIN =====");

    String indentation = """
              Java
                Study Guide
            """;

    showWhitespace(indentation);

    /*
     * The closing delimiter is further LEFT than the content.
     *
     * Its position therefore helps determine the incidental
     * indentation.
     *
     * The additional indentation in the content remains
     * essential whitespace.
     */


    System.out.println();
    System.out.println("===== TRAILING WHITESPACE =====");

    String normalTrailingSpaces = """
            Java   
            """;

    String preservedTrailingSpace = """
            Java\s
            """;

    String preservedAfterNormalTrailingSpace = """
            Java  \s
            """;


    showWhitespace(normalTrailingSpaces);
    showWhitespace(preservedTrailingSpace);
    showWhitespace(preservedAfterNormalTrailingSpace);

    /*
     * Ordinary trailing whitespace is stripped.
     *
     * \s is interpreted AFTER whitespace stripping,
     * so it produces a real preserved space.
     */


    System.out.println();
    System.out.println("===== PYRAMID =====");

    String pyramid = """
              *
             * *
            * * *
            """;

    showWhitespace(pyramid);

    /*
     * Result:
     *
     * ··*
     * ·*·*
     * *·*·*
     *
     * The indentation needed to form the pyramid remains
     * part of the resulting String.
     */
}


/*
 * Makes invisible characters visible.
 *
 * space   -> ·
 * newline -> \n followed by an actual console newline
 */
void showWhitespace(String text) {

    String visible = text
            .replace(" ", "·")
            .replace("\n", "\\n\n");

    System.out.println(visible);
}