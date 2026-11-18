package rysharp.base.chapter._01_Building_Blocks.code.text_blocks;

public class TextBlockAndStringMethodsExample {
    public static void main(String[] args) {
        section("Incidental indentation");
        String text = """
                Cat
                  Dog
                Bird
                """;
        show("text block", text);
        show("text.indent(-2)", text.indent(-2));

        section("Closing delimiter positions");
        String right = """
                Hello
                World
                    """;
        String left = """
                Hello
                World
            """;
        String sameLine = """
                Hello
                World""";
        show("delimiter right", right);
        show("delimiter left", left);
        show("delimiter same line", sameLine);

        section("Trailing whitespace, \\s, line continuation");
        String stripped = """
                Cat     
                Dog
                """;
        String preserved = """
                Cat    \s
                Dog
                """;
        String joined = """
                Cat\s\
                Dog
                """;
        show("trailing spaces stripped", stripped);
        show("four spaces plus \\s", preserved);
        show("\\s then continuation", joined);

        section("indent versus stripIndent: final newline trap");
        String withFinalNewline = "  Cat\n  Dog\n";
        String withoutFinalNewline = "  Cat\n  Dog";
        show("original with final newline", withFinalNewline);
        show("indent(-2)", withFinalNewline.indent(-2));
        show("stripIndent() with newline", withFinalNewline.stripIndent());
        show("stripIndent() without newline", withoutFinalNewline.stripIndent());
        show("indent(0) without newline", withoutFinalNewline.indent(0));
        show("indent(2) without newline", withoutFinalNewline.indent(2));

        section("strip methods and lengths");
        show("strip()", withFinalNewline.strip());
        show("stripLeading()", withFinalNewline.stripLeading());
        show("stripTrailing()", withFinalNewline.stripTrailing());
        show("trim()", withFinalNewline.trim());

        section("lines() does not count final terminator as extra line");
        System.out.println("Cat\\nDog\\n => " + "Cat\nDog\n".lines().count() + " lines");
        System.out.println("Cat\\nDog\\n\\n => " + "Cat\nDog\n\n".lines().count() + " lines");

        section("Escapes: length counts resulting characters");
        show("\\n", "\n");
        show("\\\"", "\"");
        show("\\\\", "\\");
        show("three escaped quotes", "\"\"\"");
        show("translateEscapes()", "Cat\\nDog".translateEscapes());
    }

    static void section(String name) { System.out.println("\n=== " + name + " ==="); }
    static void show(String label, String value) {
        String visible = value.replace(" ", "·").replace("\r", "\\r").replace("\n", "\\n");
        System.out.printf("%-36s [%s] length=%d%n", label, visible, value.length());
    }
    static void check(String label, boolean ok) {
        System.out.println((ok ? "PASS: " : "FAIL: ") + label);
    }
}
