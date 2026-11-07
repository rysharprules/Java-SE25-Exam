
package rysharp.exam.actionplan.enthuware;

import java.util.*;

public class SequencedCollectionExamples {

    public static void main(String[] args) {
        Q1();
        Q2();
        Q3();
        Q4();
        Q5();
    }

    static void Q1() {
        // SequencedCollection supports first/last operations, not indexed get().
        SequencedCollection<String> sc = new ArrayList<>();
        sc.add("A");
        sc.add("B");
        sc.addFirst("Z");

        System.out.println(sc.getFirst()); // Z
        System.out.println(sc.getLast());  // B

        // sc.get(0); // DOES NOT COMPILE
        System.out.println(((List<String>) sc).get(0)); // Z
    }

    static void Q2() {
        // LinkedHashSet preserves encounter order and removes duplicates.
        SequencedSet<String> set = new LinkedHashSet<>();
        set.add("Math");
        set.add("Chemistry");
        set.add("Physics");
        set.add("English");
        set.add("Math"); // Existing element: position unchanged

        System.out.println(set); // [Math, Chemistry, Physics, English]

        set.addFirst("English"); // Existing element moves to the front
        System.out.println(set); // [English, Math, Chemistry, Physics]

        set.addLast("Math"); // Existing element moves to the end
        System.out.println(set); // [English, Chemistry, Physics, Math]
    }

    static void Q3() {
        // TreeSet implements SequencedSet but maintains sorted order.
        SequencedSet<String> set = new TreeSet<>();
        set.add("Physics");
        set.add("Math");
        set.add("English");

        System.out.println(set); // [English, Math, Physics]

        // Compiles, but throws UnsupportedOperationException at runtime.
        try {
            set.addFirst("Chemistry");
        } catch (UnsupportedOperationException e) {
            System.out.println("TreeSet does not support addFirst()");
        }
    }

    static void Q4() {
        // reversed() returns a backed view, not an independent copy.
        SequencedCollection<String> original = new ArrayList<>(
                List.of("A", "B", "C"));

        SequencedCollection<String> reversed = original.reversed();

        System.out.println(reversed); // [C, B, A]

        reversed.addFirst("D"); // Adds at the END of original

        System.out.println(original); // [A, B, C, D]
        System.out.println(reversed); // [D, C, B, A]
    }

    static void Q5() {
        // Not every Collection is a SequencedCollection.
        Collection<String> hashSet = new HashSet<>();
        hashSet.add("A");

        // SequencedCollection<String> sc = new HashSet<>(); // DOES NOT COMPILE

        SequencedCollection<String> linkedSet = new LinkedHashSet<>();
        SequencedCollection<String> treeSet = new TreeSet<>();
        SequencedCollection<String> arrayList = new ArrayList<>();
        SequencedCollection<String> deque = new ArrayDeque<>();

        System.out.println(linkedSet.isEmpty());  // true
        System.out.println(treeSet.isEmpty());    // true
        System.out.println(arrayList.isEmpty());  // true
        System.out.println(deque.isEmpty());      // true
    }
}
