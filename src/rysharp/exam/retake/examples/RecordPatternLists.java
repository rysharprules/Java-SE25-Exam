import java.util.List;
public class RecordPatternLists {
    record Person(String name, List<String> hobbies) {}
    static void inspect(Object value) {
        if (value instanceof Person(String name, List<String> hobbies)) {
            System.out.println(name + " -> " + hobbies.get(0));
        }
    }
    public static void main(String[] args) {
        inspect(new Person("Alex", List.of("Chess")));
        Object value = List.of("x");
        if (value instanceof List<?> list) System.out.println(list.size());
        // Object instanceof List<String> is generally illegal: non-reifiable type.
    }
}
