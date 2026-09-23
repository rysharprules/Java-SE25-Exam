package rysharp.jdk25.addendum.Scoped_Values.code;

public class ScopedValueExample {
    // A ScopedValue must be static and final
    private static final java.lang.ScopedValue<String> NAME = java.lang.ScopedValue.newInstance();

    public static void main(String[] args) {
        // Binding a value to a scope
        java.lang.ScopedValue.where(NAME, "Alice").run(() -> {
            System.out.println("Inside scope: " + NAME.get());
            methodA();

            // Nested scope
            java.lang.ScopedValue.where(NAME, "Bob").run(() -> {
                System.out.println("Inside nested scope: " + NAME.get());
            });

            System.out.println("Back in outer scope: " + NAME.get());
        });

        // NAME is unbound here
        try {
            System.out.println("Outside: " + NAME.get());
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Outside: No binding found (as expected)");
        }
    }

    private static void methodA() {
        System.out.println("methodA sees: " + NAME.get());
    }
}
