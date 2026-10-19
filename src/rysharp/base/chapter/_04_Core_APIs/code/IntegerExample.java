package rysharp.base.chapter._04_Core_APIs.code;

public class IntegerExample {

    public static void main(String[] args) {
        example1_parseInt();
        example2_valueOf();
        example3_decode();
        example4_boxing();
        example5_comparison();
        example6_conversions();
        example7_unsigned();
    }

    // 1. parseInt() returns a primitive int
    private static void example1_parseInt() {
        System.out.println("\n1. parseInt()");

        int decimal = Integer.parseInt("42");
        int binary = Integer.parseInt("1010", 2);
        int hex = Integer.parseInt("FF", 16); // Integer.parseInt("0xFF", 16) throws NumberFormatException

        System.out.println(decimal); // 42
        System.out.println(binary);  // 10
        System.out.println(hex);     // 255

        // Integer.parseInt("0xFF"); // NumberFormatException
    }

    // 2. valueOf() returns an Integer object
    private static void example2_valueOf() {
        System.out.println("\n2. valueOf()");

        Integer a = Integer.valueOf("42");
        Integer b = Integer.valueOf(42);
        //smaller types are widened
        Integer c = Integer.valueOf((char)2);
        Integer d = Integer.valueOf((byte)2);
        Integer e = Integer.valueOf((short)2);
        // float, double & long do not compile
//        Integer f = Integer.valueOf(2.0f);
//        Integer g = Integer.valueOf(2.0);
//        Integer h = Integer.valueOf(12L);

        System.out.println(a); // 42
        System.out.println(b); // 42
    }

    // 3. decode() recognises decimal, hex and octal prefixes
    private static void example3_decode() {
        System.out.println("\n3. decode()");

        Integer decimal = Integer.decode("42");

        // Integer.decode() accepts both 0x (or 0X) and # as hexadecimal prefixes.
        Integer hex = Integer.decode("0x2A");
        Integer hexAlternative = Integer.decode("#2A");// # is recognised by Integer.decode() but is not valid syntax for a Java hexadecimal literal.
        int a = 0xFF;             // Valid Java literal
        //int b = #FF;              // Compilation error

        Integer octal = Integer.decode("052");

        System.out.println(decimal);        // 42
        System.out.println(hex);            // 42
        System.out.println(hexAlternative); // 42
        System.out.println(octal);          // 42

        // Unlike parseInt(), decode() recognises prefixes.
        // Integer.decode("0b101010"); // NumberFormatException
    }

    // 4. Autoboxing, unboxing and Integer caching
    private static void example4_boxing() {
        System.out.println("\n4. Boxing and caching");

        Integer a = 100;
        Integer b = 100;

        Integer c = 1000;
        Integer d = 1000;

        Integer e = 1000;
        int f = 1000;
        Integer g = null;

        Integer h = 128;
        Integer i = 128;

        Integer j = 127;
        Integer k = 127;

        // The cached range comes from the signed 8-bit byte range: -128 to 127.
        System.out.println(a == b);      // true
        System.out.println(c == d);      // Not guaranteed true
        System.out.println(c.equals(d)); // true
        System.out.println(e.equals(null)); // false
        //System.out.println(g.equals(f)); NPE
        //System.out.println(f == g); NPE
        System.out.println(d.equals(g)); // false
        System.out.println(e.equals(f)); // true
        System.out.println(e.intValue() == f); // true
        System.out.println(f == e); // true

        System.out.println(h == i); // false
        System.out.println(j == k); // true


        int primitive = a; // Unboxing
        Integer boxed = primitive; // Autoboxing

        System.out.println(boxed); // 100

         Integer nullValue = null;
         //int n = nullValue; // NullPointerException
    }

    // 5. Comparing Integer values
    private static void example5_comparison() {
        System.out.println("\n5. Comparison");

        Integer a = 10;
        Integer b = 20;

        System.out.println(a.compareTo(b)); // Negative
        System.out.println(Integer.compare(20, 10)); // Positive
        System.out.println(Integer.max(10, 20)); // 20
        System.out.println(Integer.min(10, 20)); // 10
    }

    // 6. Numeric conversions and formatting
    private static void example6_conversions() {
        System.out.println("\n6. Conversions");

        Integer value = 255;

        System.out.println(value.doubleValue()); // 255.0
        System.out.println(value.byteValue());   // -1 (overflow)
        System.out.println(value.shortValue());

        System.out.println(Integer.toBinaryString(10)); // 1010
        System.out.println(Integer.toHexString(255));   // ff
        System.out.println(Integer.toOctalString(8));   // 10
    }

    // 7. Signed versus unsigned operations
    private static void example7_unsigned() {
        System.out.println("\n7. Unsigned operations");

        int value = -1;

        System.out.println(Integer.toUnsignedString(value));
        // 4294967295

        System.out.println(Integer.compareUnsigned(-1, 1) > 0);
        // true

        System.out.println(Integer.divideUnsigned(-1, 2));
        // 2147483647
    }
}
