
package rysharp.base.chapter._09_Collections_and_Generics.code;

import module java.base;

public class GenericsExample {

    void main() {
        example1_invariance();
        example2_extends();
        example3_super();
        example4_unboundedWildcard();
        example5_genericMethods();
        example6_boundedTypeParameters();
        example7_typeErasure();
        example8_rawTypes();
        example9_overloading();
        example10_examTraps();
    }

    // 1. Generics are invariant
    private static void example1_invariance() {
        System.out.println("\n1. Invariance");

        List<Integer> integers = new ArrayList<>();
        integers.add(10);

        // DOES NOT COMPILE:
        // List<Number> numbers = integers;

        // Integer extends Number, but
        // List<Integer> does NOT extend List<Number>.

        List<? extends Number> numbers = integers;

        System.out.println(numbers.get(0)); // 10

        // Arrays are covariant, unlike generics.
        Number[] array = new Integer[2];

        try {
            array[0] = 3.14;
        } catch (ArrayStoreException e) {
            System.out.println("ArrayStoreException");
        }
    }

    // 2. Upper-bounded wildcard: ? extends T
    private static void example2_extends() {
        System.out.println("\n2. ? extends Number");

        List<Integer> integers = new ArrayList<>(List.of(10, 20));

        List<? extends Number> numbers = integers;

        Number n = numbers.get(0);
        System.out.println(n); // 10

        // DOES NOT COMPILE:
        // numbers.add(30);
        // numbers.add(3.14);
        // numbers.add(new Number() {});

        // Why?
        // The actual list could be List<Integer>,
        // List<Double>, List<Long>, etc.
        //
        // Java cannot safely add any particular Number.
        // Adding null IS permitted.

        numbers.add(null); // Compiles

        System.out.println(numbers); // [10, 20, null]

        // PECS: Producer Extends.
        // Use extends when you primarily READ (produce) values.
    }

    // 3. Lower-bounded wildcard: ? super T
    private static void example3_super() {
        System.out.println("\n3. ? super Integer");

        List<Number> numbers = new ArrayList<>();

        List<? super Integer> destination = numbers;

        destination.add(10);
        destination.add(20);

        Object value = destination.get(0);
        // DOES NOT COMPILE:
//        Number value1 = destination.get(0);
//        Integer value2 = destination.get(0);

        System.out.println(value); // 10

        // The actual list could be:
        // List<Integer>, List<Number> or List<Object>.
        //
        // Reading is only guaranteed to produce Object.

        // PECS: Consumer Super.
        // Use super when you primarily WRITE values.
    }

    // 4. Unbounded wildcard: ?
    private static void example4_unboundedWildcard() {
        System.out.println("\n4. Unbounded wildcard");

        List<String> strings = List.of("Java", "Kotlin");
        List<Integer> integers = List.of(1, 2, 3);
        List<?> mix = List.of("1", 2, '3');

        printList(strings);
        printList(integers);
        printList(mix);

        // List<?> accepts a List of any element type.
        // You can read values as Object.
        // You cannot add non-null values.
    }

    private static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }

        // DOES NOT COMPILE:
        // list.add("Java");

        // list.add(null) is permitted.
    }

    // 5. Generic methods
    private static void example5_genericMethods() {
        System.out.println("\n5. Generic methods");

        System.out.println(first(List.of("Java", "Kotlin"))); // Java
        System.out.println(first(List.of(10, 20)));          // 10

        String word = GenericsExample.<String>first(
                List.of("Java", "Kotlin"));

        System.out.println(word); // Java

        // The explicit <String> is optional because
        // Java can usually infer the type.
    }

    // <T> must appear BEFORE the return type.
    private static <T> T first(List<T> list) {
        return list.get(0);
    }

    // DOES NOT COMPILE:
    // private static T <T> first(List<T> list) { ... }

    // 6. Bounded type parameters
    private static void example6_boundedTypeParameters() {
        System.out.println("\n6. Bounded type parameters");

        System.out.println(max(10, 20));       // 20
        System.out.println(max("A", "Z"));      // Z

        // T must implement Comparable<T>.
        // Integer and String both satisfy this bound.

        // Multiple bounds:
        //
        // <T extends Number & Comparable<T>>
        //
        // Class bound must come first.
        // Interface bounds follow using &.
        //
        // DOES NOT COMPILE:
        // <T extends Comparable<T> & Number>
        System.out.println(maxNumber(10, 20));       // 20
    }

    private static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
    private static <T extends Number & Comparable<T>> T maxNumber(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    // 7. Type erasure
    private static void example7_typeErasure() {
        System.out.println("\n7. Type erasure");

        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();

        System.out.println(strings.getClass() == integers.getClass());
        // true

        // Generic type arguments are erased at runtime.
        //
        // DOES NOT COMPILE:
        // if (strings instanceof List<String>) { }
        //
        // Valid:
        if (strings instanceof List<?>) {
            System.out.println("It's a List");
        }

        // DOES NOT COMPILE:
        // List<String>[] array = new List<String>[3];
        //
        // Generic array creation is prohibited.
    }

    // 8. Raw types
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void example8_rawTypes() {
        System.out.println("\n8. Raw types");

        List<String> strings = new ArrayList<>();
        strings.add("Java");

        List raw = strings;
        raw.add(42); // Compiles with unchecked warning

        System.out.println(strings); // [Java, 42]

        try {
            for (String value : strings) {
                System.out.println(value);
            }
        } catch (ClassCastException e) {
            System.out.println("ClassCastException");
        }

        // Raw types bypass generic compile-time safety.
        // The problem can appear later at runtime.
    }

    // 9. Overloading and type erasure
    private static void example9_overloading() {
        System.out.println("\n9. Overloading");

        process(List.of("Java"));
        process(new ArrayList<Integer>());

        // DOES NOT COMPILE if both overloads exist:
        //
        // void broken(List<String> values) { }
        // void broken(List<Integer> values) { }
        //
        // Both erase to broken(List).
    }

    private static void process(List<String> values) {
        System.out.println("List<String>");
    }

    private static void process(ArrayList<Integer> values) {
        System.out.println("ArrayList<Integer>");
    }

    // 10. Exam traps
    private static void example10_examTraps() {
        System.out.println("\n10. Exam traps");

        // Generic classes:
        //
        // class Box<T> {
        //     private T value;
        // }
        //
        // T is available to instance members.
        //
        // DOES NOT COMPILE:
        // class Box<T> {
        //     static T value;
        // }
        //
        // Static members cannot reference the class's T.

        // Primitive type arguments are not allowed:
        //
        // DOES NOT COMPILE:
        // List<int> numbers = new ArrayList<>();
        //
        // Use List<Integer> instead.

        // Wildcards are valid in parameterized types:
        //
        // List<?>
        // List<? extends Number>
        // List<? super Integer>
        //
        // DOES NOT COMPILE:
        // class Box<?> { }
        //
        // Type parameter declarations require identifiers
        // such as T, E, K or V.

        // Generic method syntax:
        //
        // public static <T> T identity(T value)
        //
        // Type parameter comes before the return type.
    }
}
