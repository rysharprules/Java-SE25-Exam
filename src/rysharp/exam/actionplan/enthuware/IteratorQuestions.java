
package rysharp.exam.actionplan.enthuware;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class IteratorQuestions {

    public static void main(String[] args) {
        Q1();
        Q2();
        Q3();
        Q4();
        Q5();Q6();
    }

    static void Q1() {
        System.out.println("\nQ1 - Basic Iterator API");

        List<String> items = List.of("A", "B", "C");
        Iterator<String> it = items.iterator();

        System.out.println(it.hasNext()); // true
        System.out.println(it.next());    // A
        System.out.println(it.next());    // B
        System.out.println(it.next());    // C
        System.out.println(it.hasNext()); // false

        try {
            it.next();
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException");
        }
    }

    static void Q2() {
        System.out.println("\nQ2 - forEachRemaining()");

        List<String> items = List.of("A", "B", "C", "D");
        Iterator<String> it = items.iterator();

        System.out.println(it.next()); // A

        // Consumes all remaining elements.
        it.forEachRemaining(System.out::print); // BCD
        System.out.println();

        System.out.println(it.hasNext()); // false
    }

    static void Q3() {
        System.out.println("\nQ3 - Mock-style example");

        Collection<String> list = new ArrayList<>();
        Collection<String> set = new TreeSet<>();

        String[] values = {"Book", "Pen", "Pen", "Paper"};

        list.addAll(Arrays.asList(values));
        set.addAll(Arrays.asList(values));

        StringBuilder sb = new StringBuilder();

        list.iterator().forEachRemaining(s -> sb.append(s));
        sb.append("\n");
        set.iterator().forEachRemaining(sb::append);

        System.out.println(sb);
        // BookPenPenPaper
        // BookPaperPen

        // append() returns StringBuilder, but its return
        // value may be discarded by a Consumer lambda.
    }

    static void Q4() {
        System.out.println("\nQ4 - Iterator.remove()");

        List<String> items = new ArrayList<>(
                List.of("A", "B", "C"));

        Iterator<String> it = items.iterator();

        while (it.hasNext()) {
            String value = it.next();

            if (value.equals("B")) {
                it.remove(); // Removes last returned element
            }
        }

        System.out.println(items); // [A, C]

        // remove() before next(), or twice after one next(),
        // throws IllegalStateException for this iterator.
    }

    static void Q5() {
        System.out.println("\nQ5 - ConcurrentModificationException");

        List<String> items = new ArrayList<>(
                List.of("A", "B", "C"));

        try {
            for (String item : items) {
                if (item.equals("A")) {
                    items.remove(item); // Structural modification
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException");
        }

        // Use Iterator.remove() instead of modifying
        // an ArrayList directly during iteration.
        // Alternatively below, use CopyOnWriteArrayList
    }

    static void Q6() {
        System.out.println("\nQ6 - CopyOnWriteArrayList");

        List<String> items = new CopyOnWriteArrayList<>(
                List.of("A", "B", "C"));

        for (String item : items) {
            System.out.println("Visiting: " + item);

            if (item.equals("A")) {
                items.remove(item);
                items.add("D");
            }
        }

        System.out.println(items); // [B, C, D]
    }
}
