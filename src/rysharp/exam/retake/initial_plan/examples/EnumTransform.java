import java.util.function.UnaryOperator;
public class EnumTransform {
    enum Transform {
        UPPER(String::toUpperCase), LOWER(String::toLowerCase), KEEP(s -> s);
        private final UnaryOperator<String> op;
        Transform(UnaryOperator<String> op) { this.op = op; }
        String apply(String s) { return op.apply(s); }
    }
    static String attempt(String name, String value) {
        try { return Transform.valueOf(name).apply(value); }
        catch (IllegalArgumentException e) { return "snow"; }
    }
    public static void main(String[] args) {
        System.out.println(attempt("KEEP", "Hello")); // Hello
        System.out.println(attempt("UPPER", "and"));  // AND
        System.out.println(attempt("LOWER", "WORLD")); // world
        System.out.println(attempt("lower", "WORLD")); // snow (case-sensitive)
    }
}
