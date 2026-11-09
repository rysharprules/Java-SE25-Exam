package rysharp.exam.actionplan.enthuware;

import java.util.*;

public class CollectionMutabilityQuestions {

    public static void main(String[] args) {
        Q1();
        Q2();
        Q3();
        Q4();
        Q5();
        Q6();
        Q7();
        Q8();
    }

    // Q1 - List.of() creates an unmodifiable list
    static void Q1() {
        System.out.println("\nQ1 - List.of()");

        List<Integer> list = List.of(30, 10, 20);

        try {
            list.sort(Integer::compare);
        } catch (UnsupportedOperationException e) {
            System.out.println("sort(): UnsupportedOperationException");
        }

        try {
            list.set(0, 99);
        } catch (UnsupportedOperationException e) {
            System.out.println("set(): UnsupportedOperationException");
        }

        try {
            list.add(40);
        } catch (UnsupportedOperationException e) {
            System.out.println("add(): UnsupportedOperationException");
        }
    }

    // Q2 - List.copyOf() also creates an unmodifiable list
    static void Q2() {
        System.out.println("\nQ2 - List.copyOf()");

        List<Integer> original = new ArrayList<>(
                List.of(30, 10, 20));

        List<Integer> copy = List.copyOf(original);

        original.add(40);

        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);

        try {
            copy.sort(Integer::compare);
        } catch (UnsupportedOperationException e) {
            System.out.println("copy.sort(): UnsupportedOperationException");
        }
    }

    // Q3 - Arrays.asList() is fixed-size, NOT unmodifiable
    static void Q3() {
        System.out.println("\nQ3 - Arrays.asList()");

        List<Integer> list = Arrays.asList(30, 10, 20);

        list.set(0, 99);
        list.sort(Integer::compare);

        System.out.println(list); // [10, 20, 99]

        try {
            list.add(40);
        } catch (UnsupportedOperationException e) {
            System.out.println("add(): UnsupportedOperationException");
        }
    }

    // Q4 - ArrayList is mutable
    static void Q4() {
        System.out.println("\nQ4 - ArrayList");

        List<Integer> list = new ArrayList<>(
                List.of(30, 10, 20));

        list.sort(Integer::compare);
        list.set(0, 99);
        list.add(40);

        System.out.println(list); // [99, 20, 30, 40]
    }

    // Q5 - Stream.sorted() versus List.sort()
    static void Q5() {
        System.out.println("\nQ5 - sorted() versus sort()");

        List<Integer> original = List.of(30, 10, 20);

        // Returns a new unmodifiable list
        List<Integer> sorted = original.stream()
                .sorted()
                .toList();

        System.out.println("Original: " + original);
        System.out.println("Sorted: " + sorted);

        try {
            sorted.add(2);
            //sorted.sort(Integer::compare);
        } catch (UnsupportedOperationException e) {
            System.out.println("sorted.sort(): UnsupportedOperationException");
        }
    }

    static void Q6() {
        System.out.println("\nQ6 - Null handling");

        // List.of() rejects null
        try {
            List<String> list = List.of("A", null, "C");
        } catch (NullPointerException e) {
            System.out.println("List.of(): NullPointerException");
        }

        // List.copyOf() also rejects null
        try {
            List<String> original = Arrays.asList("A", null, "C");
            List<String> copy = List.copyOf(original);
        } catch (NullPointerException e) {
            System.out.println("List.copyOf(): NullPointerException");
        }

        // Arrays.asList() permits null
        List<String> fixed = Arrays.asList("A", null, "C");
        System.out.println(fixed);

        // ArrayList permits null
        List<String> mutable = new ArrayList<>(fixed);
        System.out.println(mutable);
    }

    static void Q7() {
        System.out.println("\nQ7 - Unmodifiable vs immutable");

        StringBuilder sb = new StringBuilder("Hello");

        List<StringBuilder> list = List.of(sb);

        // Cannot replace the element
        try {
            list.set(0, new StringBuilder("World"));
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot replace element");
        }

        // But the element itself is mutable!
        sb.append(" World");

        System.out.println(list); // [Hello World]
    }

    static void Q8() {
        System.out.println("\nQ8 - Unmodifiable view vs copy");

        List<String> original = new ArrayList<>(
                List.of("A", "B", "C"));

        List<String> view = Collections.unmodifiableList(original);
        List<String> copy = List.copyOf(original);

        // Cannot modify either directly
        try {
            view.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("View cannot be modified");
        }

        // But the original list CAN change
        original.add("D");

        System.out.println("Original: " + original); // [A, B, C, D]
        System.out.println("View:     " + view);     // [A, B, C, D]
        System.out.println("Copy:     " + copy);     // [A, B, C]
    }
}