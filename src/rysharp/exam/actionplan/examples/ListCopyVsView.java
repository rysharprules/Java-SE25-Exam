package rysharp.exam.actionplan.examples;

import java.util.*;
public class ListCopyVsView {
    public static void main(String[] args) {
        List<String> original = new ArrayList<>(List.of("Alice", "Bob"));

        List<String> snapshot = List.copyOf(original);
        IO.println(original == snapshot ); // false
        IO.println(original.equals(snapshot)); // true

        List<String> view = Collections.unmodifiableList(original);
        IO.println(original == view ); // false
        IO.println(original.equals(view)); // true

        original.set(0, "Charlie");
        System.out.println(original);// [Charlie, Bob]

        // copyOf does not reflect changes
        System.out.println(snapshot.get(0)); // Alice
        IO.println(original.equals(snapshot)); // false

        // view reflects changes in original mutable list
        System.out.println(view.get(0));     // Charlie
        IO.println(original.equals(view)); // true

        try {
            view.set(0, "Sam");
        } catch (Exception e) {
            System.out.println("View set error: " + e);
        }

        try {
            Collections.sort(view);
        } catch (Exception e) {
            System.out.println("View sort error: " + e);
        }

        try {
            snapshot.set(0, "Marcus");
        } catch (Exception e) {
            IO.println("copyOf set error: " + e);
        }

        try {
            snapshot.add("Jim");
        } catch (Exception e) {
            IO.println("copyOf add error: " + e);
        }
    }
}
