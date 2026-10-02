/*
 * Demonstrates the basic rules of Java text blocks.
 *
 * Things to notice:
 *
 *  - A text block is still a String.
 *  - The opening """ must be followed by a line terminator.
 *  - Ordinary " characters don't need escaping.
 *  - The position of the closing """ affects the final newline.
 */

void main() {

    System.out.println("===== BASIC TEXT BLOCK =====");

    String basic = """
            Java
            Study
            Guide
            """;

    System.out.print(basic);

    /*
     * Equivalent to:
     *
     * "Java\nStudy\nGuide\n"
     *
     * because the closing delimiter is on the next line.
     */

    System.out.println("Length: " + basic.length()); // 17
    System.out.println("Java\nStudy\nGuide\n".length()); // 17


    System.out.println();
    System.out.println("===== QUOTES =====");

    String quotes = """
            "Java Study Guide"
            by Jeanne & Scott
            """;

    /*
     * Ordinary double quotes do not need escaping
     * inside a text block.
     */

    System.out.print(quotes);


    System.out.println();
    System.out.println("===== CLOSING DELIMITER =====");

    String withFinalNewline = """
            Hello
            World
            """;

    String withoutFinalNewline = """
            Hello
            World""";

    /*
     * replace() makes the otherwise invisible newline visible.
     */
    System.out.println(
            "withFinalNewline    = "
                    + withFinalNewline.replace("\n", "\\n")
    );

    System.out.println(
            "withoutFinalNewline = "
                    + withoutFinalNewline.replace("\n", "\\n")
    );


    System.out.println();
    System.out.println("===== STILL A STRING =====");

    String text = """
            hello world""";

    System.out.println(text.toUpperCase());
    System.out.println(text.contains("world"));
    System.out.println(text.length());


    /*
     * DOES NOT COMPILE:
     *
     * String invalid = """hello""";
     *
     * The opening delimiter must be followed by a
     * line terminator.
     */
}