import java.util.*;
public class ListCopyVsView {
    public static void main(String[] args) {
        List<String> original = new ArrayList<>(List.of("Alice", "Bob"));
        List<String> snapshot = List.copyOf(original);
        List<String> view = Collections.unmodifiableList(original);
        original.set(0, "Charlie");
        System.out.println(snapshot.get(0)); // Alice
        System.out.println(view.get(0));     // Charlie
    }
}
