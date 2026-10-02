/*
 * Demonstrates escape sequences inside text blocks.
 *
 * Particularly important:
 *
 *      \s          preserved space
 *
 *      \           at the end of a source line
 *                  suppresses that line's newline
 *
 *      \n          explicitly adds a newline
 *
 *      \"          represents "
 *
 * Text-block escape sequences are interpreted AFTER
 * incidental whitespace has been removed.
 */

void main() {

    System.out.println("===== LINE CONTINUATION =====");

    String joined = """
            doe \
            deer""";

    showWhitespace(joined);

    /*
     * Result:
     *
     * doe·deer
     *
     * NOT:
     *
     * doe
     * deer
     *
     * The \ at the end of the source line suppresses
     * the physical newline.
     */

    System.out.println();
    System.out.println("===== WITHOUT LINE CONTINUATION =====");

    String separate = """
            doe
            deer""";

    showWhitespace(separate);

    /*
     * Result:
     *
     * doe\n
     * deer
     */


    System.out.println();
    System.out.println("===== EXPLICIT NEWLINE =====");

    String explicitNewline = """
            doe\n
            deer""";

    showWhitespace(explicitNewline);

    /*
     * Be careful here.
     *
     * There are TWO newlines between doe and deer:
     *
     *     1. \n explicitly creates one
     *     2. the physical source newline creates another
     *
     * Result:
     *
     * doe\n
     * \n
     * deer
     */


    System.out.println();
    System.out.println("===== PRESERVED SPACE =====");

    String preservedSpace = """
            Java\s
            Rocks""";

    showWhitespace(preservedSpace);

    /*
     * Result:
     *
     * Java·\n
     * Rocks
     *
     * The space produced by \s survives.
     */


    System.out.println();
    System.out.println("===== ORDINARY QUOTES =====");

    String quotes = """
            "Java"
            "Python"
            "Kotlin"
            """;

    System.out.print(quotes);

    /*
     * Individual " characters don't need escaping.
     */


    System.out.println();
    System.out.println("===== THREE QUOTES =====");

    String tripleQuotes = """
            Java says: \"""
            """;

    System.out.print(tripleQuotes);

    /*
     * Produces:
     *
     * Java says: """
     *
     * At least one quote is escaped so that the sequence
     * isn't interpreted as the closing delimiter.
     */


    System.out.println();
    System.out.println("===== PROCESSING ORDER =====");

    String processing = """
            A\s
            B\
            C
            """;

    showWhitespace(processing);

    /*
     * Remember the compiler's conceptual processing order:
     *
     * 1. Normalize line terminators
     *
     * 2. Remove incidental whitespace
     *
     * 3. Interpret escape sequences
     *
     * Therefore:
     *
     * \s creates its space AFTER whitespace stripping.
     *
     * \<line-terminator> removes that newline AFTER
     * whitespace processing.
     */
}


void showWhitespace(String text) {

    String visible = text
            .replace(" ", "·")
            .replace("\n", "\\n\n")
            .replace("\t", "\\t");

    System.out.println(visible);
}