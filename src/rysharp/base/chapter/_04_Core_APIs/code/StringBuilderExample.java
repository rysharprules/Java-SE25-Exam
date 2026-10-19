package rysharp.base.chapter._04_Core_APIs.code;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringBuilderExample {

    public static void main(String[] args) {
        example1_constructors();
        example2_appendInsert();
        example3_deleteReplace();
        example4_substringSubSequence();
        example5_charsCodePoints();
        example6_capacityLength();
        example7_mutability();
        example8_comparison();
        example9_otherMethods();
        example10_examTraps();
    }

    // 1. Constructors
    private static void example1_constructors() {
        System.out.println("\n1. Constructors");

        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder(50);
        StringBuilder c = new StringBuilder("Hello");
        StringBuilder d = new StringBuilder((CharSequence) "World");

        System.out.println(a.length());   // 0
        System.out.println(a.capacity()); // 16

        System.out.println(b.length());   // 0
        System.out.println(b.capacity()); // 50

        System.out.println(c.length());   // 5
        System.out.println(c.capacity()); // 21

        System.out.println(d);            // World
        System.out.println(d.capacity()); // 21

        // new StringBuilder(-1); // NegativeArraySizeException
    }

    // 2. append() and insert() modify the same object
    private static void example2_appendInsert() {
        System.out.println("\n2. Append and insert");

        StringBuilder sb = new StringBuilder("Java");

        sb.append(25);
        System.out.println(sb); // Java25

        sb.insert(4, " ");
        System.out.println(sb); // Java 25

        sb.append(true);
        System.out.println(sb); // Java 25true

        // append(null) can be ambiguous with overloaded methods:
        // sb.append(null); // Warning: may throw NPE via char[] overload

        sb.append((String) null);
        System.out.println(sb); // Java 25truenull
    }

    // 3. delete(), deleteCharAt(), replace()
    private static void example3_deleteReplace() {
        System.out.println("\n3. Delete and replace");

        StringBuilder sb = new StringBuilder("0123456789");

        sb.delete(2, 5);
        System.out.println(sb); // 0156789

        sb.deleteCharAt(0);
        System.out.println(sb); // 156789

        sb.replace(1, 3, "XXXX");
        System.out.println(sb); // 1XXXX789

        // End index is exclusive for delete and replace.
    }

    // 4. substring() versus subSequence()
    private static void example4_substringSubSequence() {
        System.out.println("\n4. Substring and subSequence");

        StringBuilder sb = new StringBuilder("Certification");

        String a = sb.substring(0, 4);
        String b = sb.substring(4);
        CharSequence c = sb.subSequence(0, 4);

        System.out.println(a); // Cert
        System.out.println(b); // ification
        System.out.println(c); // Cert

        System.out.println(a.getClass().getSimpleName()); // String
        System.out.println(c.getClass().getSimpleName()); // String

        sb.replace(0, 4, "Test");

        System.out.println(sb); // Testification
        System.out.println(a);  // Cert (unchanged)
        System.out.println(c);  // Cert (unchanged)

        // substring() and subSequence() do NOT mutate StringBuilder.
        // substring() returns String.
        // subSequence() returns CharSequence (actually String here).
    }

    // 5. chars() and codePoints() - PE3 Q37
    private static void example5_charsCodePoints() {
        System.out.println("\n5. chars() and codePoints()");

        StringBuilder sb = new StringBuilder("ABC");

        // chars() returns IntStream, NOT char[]
        int[] values = sb.chars().toArray();

        System.out.println(Arrays.toString(values));
        // [65, 66, 67]

        String reconstructed = sb.chars()
                .mapToObj(Character::toString)
                .collect(Collectors.joining());

        System.out.println(reconstructed); // ABC

        // Convert to String:
        String text = sb.toString();

        // Convert to char[]:
        char[] characters = sb.toString().toCharArray();

        System.out.println(text); // ABC
        System.out.println(Arrays.toString(characters));
        // [A, B, C]

        // Unicode supplementary character (uses surrogate pair)
        StringBuilder emoji = new StringBuilder("A😀");

        System.out.println(emoji);
        System.out.println(emoji.length()); // 3 UTF-16 code units

        System.out.println(emoji.chars().count());      // 3
        System.out.println(emoji.codePoints().count()); // 2

        // chars() streams UTF-16 code units.
        // codePoints() streams Unicode code points.
    }


    // 6. length() versus capacity() - PE4 Q14
    private static void example6_capacityLength() {
        System.out.println("\n6. Length and capacity");

        StringBuilder sb = new StringBuilder("abc");

        System.out.println(sb.length());   // 3
        System.out.println(sb.capacity()); // 19 (16 + 3)

        sb.ensureCapacity(40);
        System.out.println(sb.capacity()); // 40

        sb.setLength(5);
        System.out.println(sb.length()); // 5

        System.out.println((int) sb.charAt(0)); // 97
        System.out.println((int) sb.charAt(4)); // 0
        System.out.println(sb); // abc\u0000\u0000 (escape sequence for the null character (NUL))
        // System.out.println((int) sb.charAt(5)); // StringIndexOutOfBoundsException: Index 5 out of bounds for length 5

        sb.setLength(2);
        System.out.println(sb); // ab

        sb.trimToSize(); // attempts to reduce the capacity of a StringBuilder to match its current length, freeing any unused storage.
        System.out.println(sb.capacity()); // 2
        System.out.println("trimToSize");
        StringBuilder trimToSize = new StringBuilder(50);

        trimToSize.append("Java");

        System.out.println(trimToSize.length());   // 4
        System.out.println(trimToSize.capacity()); // 50

        trimToSize.trimToSize();

        System.out.println(trimToSize.length());   // 4
        System.out.println(trimToSize.capacity()); // Typically 4
        System.out.println(trimToSize);            // Java

        // --------------------------------------------------
        // PE4 Q14 - Automatic capacity growth
        // --------------------------------------------------
        System.out.println("Automatic capacity growth");
        // Constructor argument specifies CAPACITY, not length.
        StringBuilder builder = new StringBuilder(8);

        System.out.println(builder.length());   // 0
        System.out.println(builder.capacity()); // 8

        // Append 12 characters, exceeding capacity 8.
        builder.append("123456789012");

        System.out.println(builder.length());   // 12
        System.out.println(builder.capacity()); // 18

        // Growth formula:
        // newCapacity = max(oldCapacity * 2 + 2, requiredLength)
        //
        // oldCapacity    = 8
        // requiredLength = 12
        //
        // max(8 * 2 + 2, 12)
        // max(18, 12) = 18

        // If the required length exceeds the growth formula,
        // the capacity grows directly to the required length.

        StringBuilder large = new StringBuilder(8);
        large.append("12345678901234567890");

        System.out.println(large.length());   // 20
        System.out.println(large.capacity()); // 20

        // max(8 * 2 + 2, 20)
        // max(18, 20) = 20

        new StringBuilder();        // length 0, capacity 16
        new StringBuilder(10);      // length 0, capacity 10
        var norm = new StringBuilder("Java");  // length 4, capacity 20
        norm.append("25");
        System.out.println(norm.capacity());


        // --------------------------------------------------
        // Exam reminders
        // --------------------------------------------------

        // length()   = number of characters currently stored.
        // capacity() = allocated space before resizing is needed.

        // new StringBuilder(8) starts EMPTY with capacity 8.
        // new StringBuilder("12345678") starts with length 8
        // and capacity 24 (16 + 8).

        // setLength() changes length, not necessarily capacity.
        // ensureCapacity() increases capacity if necessary.
        // trimToSize() attempts to reduce capacity to length.
        // append() automatically expands capacity when required.
    }


    // 7. StringBuilder is mutable
    private static void example7_mutability() {
        System.out.println("\n7. Mutability");

        StringBuilder a = new StringBuilder("Java");
        StringBuilder b = a;

        a.append("25");

        System.out.println(a); // Java25
        System.out.println(b); // Java25

        StringBuilder c = new StringBuilder("Java");

        c.append("25");
        System.out.println(c); // Java25
    }

    // 8. equals(), == and compareTo()
    private static void example8_comparison() {
        System.out.println("\n8. Comparison");

        StringBuilder a = new StringBuilder("Java");
        StringBuilder b = new StringBuilder("Java");
        StringBuilder c = a;

        System.out.println(a == b);      // false
        System.out.println(a.equals(b)); // false
        System.out.println(a == c);      // true

        System.out.println(a.compareTo(b)); // 0

        // StringBuilder does not override equals().
        // compareTo() compares character content.
        // StringBuilder implements Comparable<StringBuilder>.
    }

    // 9. Other useful methods
    private static void example9_otherMethods() {
        System.out.println("\n9. Other methods");

        StringBuilder sb = new StringBuilder("Java Java");

        System.out.println(sb.charAt(0));       // J
        System.out.println(sb.indexOf("Java"));  // 0
        System.out.println(sb.lastIndexOf("Java")); // 5

        sb.setCharAt(0, 'K');
        System.out.println(sb); // Kava Java

        sb.reverse();
        System.out.println(sb); // avaJ avaK

        StringBuilder copy = new StringBuilder("ABCDE");
        char[] destination = new char[3];
        char[] destination2 = new char[]{'1','2','3','4','5'};

        copy.getChars(1, 4, destination, 0);
        System.out.println(Arrays.toString(destination));
        // [B, C, D]
        copy.getChars(1, 4, destination2, 2);
        System.out.println(Arrays.toString(destination2));
        // [1, 2, B, C, D]
    }

    // 10. Common certification traps
    private static void example10_examTraps() {
        System.out.println("\n10. Exam traps");

        StringBuilder sb = new StringBuilder("ABCDE");

        // Index 1 inclusive, index 3 exclusive
        System.out.println(sb.substring(1, 3)); // BC

        // delete() allows an end index beyond length
        sb.delete(2, 100);
        System.out.println(sb); // AB

        // insert() allows index == length
        sb.insert(sb.length(), "C");
        System.out.println(sb); // ABC

        // append() returns the same StringBuilder reference
        StringBuilder result = sb.append("D");

        System.out.println(result == sb); // true

        // This does not compile:
        // String s = new String(sb.chars());

        // chars() returns IntStream, not char[].

        // This does not compile:
        // sb.substring(0, 2).append("X");

        // substring() returns immutable String.

        // This does not compile:
        // sb.subset(0, 2);

        // There is no subset() method.
    }
}
